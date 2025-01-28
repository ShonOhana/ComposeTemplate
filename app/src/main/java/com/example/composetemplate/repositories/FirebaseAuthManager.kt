package com.example.composetemplate.repositories

import android.util.Base64
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.data.models.local_models.GoogleCredentialAuthParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.models.server_models.PermissionType
import com.example.composetemplate.data.remote.confing.FirebaseConfigProvider
import com.example.composetemplate.data.remote.confing.remoteConfigVal
import com.example.composetemplate.data.remote.errors.AuthError
import com.example.composetemplate.managers.TokenData
import com.example.composetemplate.managers.TokenFetcher
import com.example.composetemplate.managers.TokenManager
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInResult
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpResult
import com.example.composetemplate.utils.extensions.errorType
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import org.koin.java.KoinJavaComponent.inject
import java.security.MessageDigest
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * The FirebaseActionable class handles all Firebase authentication tasks.
 * It manages user sign-up, sign-in, password reset, and token fetching using Firebase Authentication SDK.
 * This class also implements TokenFetcher to retrieve and manage the Firebase token.
 */
class FirebaseAuthManager : AuthActionable, TokenFetcher {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Registers a new user using email and password.
     *
     * @param user The user object containing user details.
     * @param password The password for the new user.
     * @return The result of the sign-up operation.
     */
    override suspend fun createUserWithEmailAndPassword(
        user: User,
        password: String
    ): SignUpResult {
        if (user.email.isEmpty()) {
            return SignUpResult.Cancelled
        }
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            if (result.user != null) {
                SignUpResult.Success(user)
            } else {
                SignUpResult.Failure(AuthError(ErrorType.USER_NOT_FOUND))
            }
        } catch (e: Exception) {
            SignUpResult.Failure(AuthError(e.errorType))
        }
    }

    /**
     * Signs in an existing user using email and password.
     *
     * @param user The user object containing user details.
     * @param password The user's password.
     * @return The result of the sign-in operation.
     */
    override suspend fun signInWithEmailAndPassword(
        user: User,
        password: String
    ): SignInResult {
        if (user.email.isEmpty()) {
            return SignInResult.Cancelled
        }
        return try {
            val result = auth.signInWithEmailAndPassword(user.email, password).await()
            if (result.user != null) {
                SignInResult.Success(user)
            } else {
                SignInResult.Failure(AuthError(ErrorType.USER_NOT_FOUND))
            }
        } catch (e: Exception) {
            SignInResult.Failure(AuthError(e.errorType))
        }
    }

    /**
     * Retrieves the current user from Firebase.
     *
     * @return The sign-in result indicating whether the user is signed in or not.
     */
    override suspend fun getUser(): SignInResult {
        return if (auth.currentUser != null)
            SignInResult.Success(null)
        else
            SignInResult.NoCredentials(AuthError(ErrorType.USER_NOT_FOUND))
    }

    /**
     * Logs out the current user from Firebase.
     */
    override fun logout() {
        auth.signOut()
        clearToken()
    }

    private fun clearToken() {
        val tokenManager by inject<TokenManager>(TokenManager::class.java)
        tokenManager.clearToken()
    }

    /**
     * Sends a password reset email to the user.
     *
     * @param email The email address to send the password reset link.
     */
    override suspend fun resetPassword(email: String) {
        suspendCancellableCoroutine { continuation ->
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit) // Resumes with success
                    } else {
                        val exception = task.exception ?: Exception("Unknown error occurred")
                        continuation.resumeWithException(exception) // Resumes with failure
                    }
                }
        }
    }

    /**
     * Fetches the current Firebase token. If the token is valid, it will be used; otherwise, a new token is fetched.
     * Firebase tokens are valid for 1 hour, so this method will refresh the token if it is expired.
     *
     * @return A [TokenData] object containing the token and its expiration time.
     */
    override suspend fun fetchToken(): TokenData {
        return suspendCancellableCoroutine { continuation ->
            auth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newToken = task.result?.token
                    val expirationTime = parseExpirationDate(newToken)
                    continuation.resume(TokenData(newToken, expirationTime))
                } else {
                    continuation.resume(TokenData(null, 0L)) // Handle failure case
                }
            }
        }
    }

    /**
     * Parses the expiration date from the Firebase ID token.
     *
     * @param token The Firebase ID token as a String.
     * @return The expiration time of the token in milliseconds.
     */
    private fun parseExpirationDate(token: String?): Long {
        return try {
            token ?: return 0L
            // Split the token into its components (header, payload, signature)
            val parts = token.split(".")
            if (parts.size != 3) {
                return 0L // Invalid token format
            }

            // Decode the payload from Base64 URL-safe encoding
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            // Parse the payload as a JSON object
            val jsonObject = JSONObject(payload)
            // Extract the expiration time (exp) and convert from seconds to milliseconds
            jsonObject.optLong("exp", 0) * 1000
        } catch (e: Exception) {
            e.printStackTrace()
            0L // Return 0 if parsing fails
        }
    }

    /**
     * Performs Google Sign-In using Google Identity Services and Firebase.
     *
     * @param googleCredentialAuthParameter Parameters needed for Google Sign-In, including the activity context.
     * @return A [SignInResult] indicating the outcome of the sign-in operation.
     */
    override suspend fun googleSignIn(googleCredentialAuthParameter: GoogleCredentialAuthParameter): SignInResult {
        return try {
            val credentialManager = CredentialManager.create(googleCredentialAuthParameter.activity)
            val googleIdOption: GetGoogleIdOption = creacteGoogleIdOptionWithSecurity()
            val request: GetCredentialRequest = createGoogleSignInRequest(googleIdOption)
            val googleIdToken = getGoogleIdTokenFromCredentialManager(credentialManager, request, googleCredentialAuthParameter)

            /* Create Firebase credentials using the Google ID token. */
            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            val user = auth.signInWithCredential(googleCredentials).await().user

            SignInResult.Success(
                User(
                    fullName = user?.displayName ?: "",
                    email = user?.email ?: "",
                    permissionType = PermissionType.DEVELOPER.name.lowercase()
                )
            )
        } catch (e: GetCredentialCancellationException) {
            e.printStackTrace()
            SignInResult.Cancelled
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            SignInResult.NoCredentials(AuthError(e.errorType))
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            SignInResult.Failure(AuthError(e.errorType))
        }
    }

    private suspend fun getGoogleIdTokenFromCredentialManager(
        credentialManager: CredentialManager,
        request: GetCredentialRequest,
        googleCredentialAuthParameter: GoogleCredentialAuthParameter
    ): String {
        val result = credentialManager.getCredential(
            request = request,
            context = googleCredentialAuthParameter.activity
        )
        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val googleIdToken = googleIdTokenCredential.idToken
        return googleIdToken
    }

    private fun createGoogleSignInRequest(googleIdOption: GetGoogleIdOption): GetCredentialRequest {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        return request
    }

    private fun creacteGoogleIdOptionWithSecurity(): GetGoogleIdOption {
        /* Generate a random nonce for security purposes. */
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()

        /* Hash the nonce using SHA-256 for additional security. */
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(FirebaseConfigProvider.getData(remoteConfigVal.WEB_CLIENT_ID))
            .setNonce(hashedNonce)
            .build()
        return googleIdOption
    }
}
