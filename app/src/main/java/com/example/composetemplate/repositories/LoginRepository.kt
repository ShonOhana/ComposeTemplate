package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.managers.NetworkManager
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback
import com.example.composetemplate.utils.extensions.isSuccessful
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 *
 * In the MVVM architecture, the Repository layer acts as a mediator between the ViewModel and data sources.
 * It aggregates data from multiple sources, such as remote APIs, databases, and caches, and provides a clean API for the ViewModel
 * This class contain the functionality of all login auth types
 */
class LoginRepository(
    private val firebaseDataSource: FirebaseDataSource,
    private val networkManager: NetworkManager,
    private val ioScope: CoroutineScope,
) : AuthDBServiceable {

    /** create user by email and password */
    fun createEmailPasswordUser(user: User, password: String, loginCallback: LoginCallback) {
        if (user.email.isEmpty().not()) {
            firebaseDataSource.auth.createUserWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // TODO: get token provider instead using only firebase
                        firebaseDataSource.getFirebaseUserAccessToken { b, exception ->
                            ioScope.launch {
                                createOrUpdateUser(user, loginCallback)
                            }
                        }
                    } else {
                        loginCallback(null, task.exception)
                    }
                }
        } else
            loginCallback(null, Exception("email is empty"))
    }

    /** sign in user by email and password */
    fun signInEmailPasswordUser(user: User, password: String, loginCallback: LoginCallback) {
        if (user.email.isEmpty().not()) {
            firebaseDataSource.auth.signInWithEmailAndPassword(user.email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // TODO: get token provider instead using only firebase
                        firebaseDataSource.getFirebaseUserAccessToken { b, exception ->
                            ioScope.launch {
                                createOrUpdateUser(user, loginCallback)
                            }
                        }
                    }  else {
                        loginCallback(null, task.exception)
                    }
                }
        } else
            loginCallback(null, Exception("email is empty"))
    }

    //logOut user in firebase
    fun logOut() = firebaseDataSource.logOut()

    // reset password
    fun resetPassword(email: String, successCallback: SuccessCallback) = firebaseDataSource.resetPassword(email,successCallback)

    override suspend fun getUser(loginCallback: LoginCallback) {
        // TODO: move to token provider so the scope will be good
        firebaseDataSource.getFirebaseUserAccessToken { b, exception ->
            ioScope.launch {
                val request = firebaseDataSource.getUserRequest()
                (networkManager.sendRequest(request) as? HttpResponse).let { response ->
                    val user = response?.body<User>()
                    if (user != null) loginCallback(user, null) else loginCallback(
                        null,
                        Exception()
                    ) //todo: check exception
                }
            }
        }
    }

    /** Create or update in database */
    override suspend fun createOrUpdateUser(user: User, loginCallback: LoginCallback) {
        val request = firebaseDataSource.createOrUpdateUserRequest(user)
        val response = (networkManager.sendRequest(request) as? HttpResponse)
        val success = response?.isSuccessful() == true
        if (success) loginCallback(user, null) else loginCallback(
            null,
            Exception()
        ) //todo: check exception
    }
}
