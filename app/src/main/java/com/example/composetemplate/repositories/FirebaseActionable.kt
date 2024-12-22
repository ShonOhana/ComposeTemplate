package com.example.composetemplate.repositories

import android.util.Base64
import com.example.composetemplate.data.local.CacheData.user
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
 * In the MVVM architecture, the DataSource layer is responsible for providing data to the repository
 * from various sources, such as remote APIs, local databases, or in-memory caches.
 * allowing the repository to seamlessly aggregate and supply data to the ViewModel without worrying about the underlying data origins or access mechanisms.
 */
class FirebaseActionable : AuthActionable, TokenFetcher {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun createUserWithEmailAndPassword(
        user: User,
        password: String
    ) : SignUpResult {
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

    override suspend fun getUser(): SignInResult {
        return if (auth.currentUser != null)
            SignInResult.Success(null)
        else
            SignInResult.NoCredentials(AuthError(ErrorType.USER_NOT_FOUND))
    }

    override fun logout() = auth.signOut()

    override suspend fun resetPassword(email: String) {
        suspendCancellableCoroutine{ continuation ->
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
//        return suspendCancellableCoroutine { continuation ->
//            auth.sendPasswordResetEmail(email)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        continuation.resume(true) // Success
//                    } else {
//                        continuation.resume(false) // Failure
//                    }
//                }
//        }
    }

    /**
     * Get the token from Firebase. if we have it and its still valid use the token that exist,
     * otherwise if he doesn't exist or not valid bring the new token.
     *
     * Firebase token valid for an hour so in that way if the user is in the app for more then an
     * hour we need to refresh the token because its not valid.
     * and we don't need to get the token in every call as long its valid
     * */
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

    /** Parse the expiration date from token in firebase mode.*/
    private fun parseExpirationDate(token: String?): Long {
        return try {
            token ?: return 0L
            // Split the token into parts: header, payload, and signature
            val parts = token.split(".")
            if (parts.size != 3) {
                return 0L // Invalid token format
            }

            // Decode the payload part from Base64
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            // Parse the payload as a JSON object
            val jsonObject = JSONObject(payload)
            // Get the expiration time (exp) from the JSON object (convert seconds to milliseconds)
            jsonObject.optLong("exp", 0) * 1000
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

}
