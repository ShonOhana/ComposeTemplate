package com.example.composetemplate.repositories

import android.util.Base64
import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.errors.AuthError
import com.example.composetemplate.managers.TokenData
import com.example.composetemplate.managers.TokenFetcher
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInResult
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpResult
import com.example.composetemplate.utils.extensions.errorType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * The FirebaseActionable class handles all Firebase authentication tasks.
 * It manages user sign-up, sign-in, password reset, and token fetching using Firebase Authentication SDK.
 * This class also implements TokenFetcher to retrieve and manage the Firebase token.
 */
class FirebaseActionable : AuthActionable, TokenFetcher {

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
}
