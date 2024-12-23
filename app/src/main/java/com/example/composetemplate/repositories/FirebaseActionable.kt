package com.example.composetemplate.repositories

import android.content.Intent
import android.content.IntentSender
import android.util.Base64
import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.data.models.local_models.GoogleAuthUiClientParameters
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.models.server_models.PermissionType
import com.example.composetemplate.data.remote.confing.FirebaseConfigProvider
import com.example.composetemplate.data.remote.confing.remoteConfigVal
import com.example.composetemplate.data.remote.errors.AuthError
import com.example.composetemplate.managers.TokenData
import com.example.composetemplate.managers.TokenFetcher
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInResult
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpResult
import com.example.composetemplate.utils.extensions.errorType
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * The FirebaseActionable class handles all Firebase authentication tasks.
 * It manages user sign-up, sign-in, password reset, and token fetching using Firebase Authentication SDK.
 * This class also implements TokenFetcher to retrieve and manage the Firebase token.
 */
class FirebaseActionable : AuthActionable,GoogleAuthActionable, TokenFetcher {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var oneTapClient: SignInClient


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
    override fun logout() = auth.signOut()

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
     * Signs in the user using an intent received from the Google One Tap client.
     *
     * This method retrieves the Google Sign-In credential from the provided intent,
     * extracts the ID token, and uses it to authenticate the user via Firebase.
     *
     * @param intent The intent received from the Google One Tap client.
     * @return A [SignInResult] object that represents the outcome of the sign-in operation.
     * - [SignInResult.Success]: Contains the user details if the sign-in is successful.
     * - [SignInResult.Cancelled]: Returned if an error occurs during the sign-in process or if the intent is null.
     * @throws CancellationException If the coroutine is canceled during the operation.
     */
    override suspend fun signInWithIntent(intent: Intent?): SignInResult {
        if (::oneTapClient.isInitialized.not()) return SignInResult.Cancelled
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult.Success(
                User(
                    fullName = user?.displayName ?: "",
                    email = user?.email ?: "",
                    permissionType = PermissionType.DEVELOPER.name.lowercase()
                )
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult.Cancelled
        }
    }

    /**
     * Opens the Google Authentication dialog using the provided One Tap client parameters.
     *
     * This method initializes the Google One Tap client and starts the sign-in process,
     * returning an [IntentSender] for launching the authentication UI.
     *
     * @param googleAuthUiClient The parameters required to initialize the Google One Tap client.
     * @return An [IntentSender] that can be used to launch the authentication dialog, or `null` if an error occurs.
     * @throws CancellationException If the coroutine is canceled during the operation.
     */
    override suspend fun openGoogleAuthDialog(googleAuthUiClient: GoogleAuthUiClientParameters): IntentSender? {
        val result = try {
            oneTapClient = googleAuthUiClient.oneTapClient
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    /**
     * Builds a [BeginSignInRequest] for Google One Tap authentication.
     *
     * This request specifies the configuration for ID token retrieval and authorized accounts.
     *
     * @return A [BeginSignInRequest] object containing the sign-in options.
     */
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(FirebaseConfigProvider.getData(remoteConfigVal.WEB_CLIENT_ID))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    /**
     * Signs out the current user from the Google One Tap client.
     *
     * This method clears the current user session for the Google One Tap client.
     * If the client is not initialized, the method returns immediately.
     */
    override fun signOut() {
        if (::oneTapClient.isInitialized.not()) return
        oneTapClient.signOut()
    }

}
