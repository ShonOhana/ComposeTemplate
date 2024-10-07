package com.example.composetemplate.repositories

import android.util.Base64
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.managers.TokenData
import com.example.composetemplate.managers.TokenFetcher
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume

/**
 * In the MVVM architecture, the DataSource layer is responsible for providing data to the repository
 * from various sources, such as remote APIs, local databases, or in-memory caches.
 * allowing the repository to seamlessly aggregate and supply data to the ViewModel without worrying about the underlying data origins or access mechanisms.
 */
class FirebaseDataSource : AuthDataSource, TokenFetcher {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun createUserWithEmailAndPassword(
        user: User,
        password: String,
        loginCallback: LoginCallback
    ) {
        if (user.email.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        loginCallback(user, null)
                    else
                        loginCallback(null, task.exception)
                }
        } else {
            loginCallback(null, Exception("Email is empty"))
        }
    }

    override fun signInWithEmailAndPassword(
        user: User,
        password: String,
        loginCallback: LoginCallback
    ) {
        if (user.email.isNotEmpty()) {
            auth.signInWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        loginCallback(user, null)
                    else
                        loginCallback(null, task.exception)
                }
        } else {
            loginCallback(null, Exception("Email is empty"))
        }
    }

    override fun getUser(loginCallback: LoginCallback) {
        if (auth.currentUser == null)
            loginCallback(null, Exception("No user"))
        else
            loginCallback(null, null)
    }

    override fun logout() = auth.signOut()

    override fun resetPassword(email: String, successCallback: SuccessCallback) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    successCallback(true, null)
                } else {
                    successCallback(false, task.exception)
                }
            }
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
