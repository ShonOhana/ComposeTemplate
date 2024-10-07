package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.data.remote.requests.FirebaseUserRequests
import com.example.composetemplate.managers.FirebaseNetworkManager
import com.example.composetemplate.managers.MainNetworkManager
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback
import com.example.composetemplate.utils.extensions.isSuccessful
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface AuthDbServiceable {
    fun createOrUpdateUser(user: User, loginCallback: LoginCallback)
    fun getUser(loginCallback: LoginCallback)
}
/**
 *
 * In the MVVM architecture, the Repository layer acts as a mediator between the ViewModel and data sources.
 * It aggregates data from multiple sources, such as remote APIs, databases, and caches, and provides a clean API for the ViewModel
 * This class contain the functionality of all login auth types
 */
class LoginRepository(
    private val authDataSource: AuthDataSource,
    private val networkManager: FirebaseNetworkManager,
    private val ioScope: CoroutineScope,
): AuthDbServiceable {

    enum class RequestType {
        CREATE_USER, GET_USER
    }

    /** create user by email and password */
    fun registerUser(user: User, password: String, loginCallback: LoginCallback) {
        authDataSource.createUserWithEmailAndPassword(user, password) { createdUser, exception ->
            if (createdUser != null && exception == null)
                createOrUpdateUser(user, loginCallback)
            else {
                loginCallback(null, exception)
            }
        }
    }

    /** sign in user by email and password */
    fun signInEmailPasswordUser(user: User, password: String, loginCallback: LoginCallback) {
        if (user.email.isEmpty().not()) {
            authDataSource.signInWithEmailAndPassword(user, password) { createdUser, exception ->
                if (createdUser != null && exception == null)
                    createOrUpdateUser(user, loginCallback)
                else {
                    loginCallback(null, exception)
                }
            }
        } else
            loginCallback(null, Exception("email is empty"))
    }

    fun logOut() = authDataSource.logout()

    fun resetPassword(email: String, successCallback: SuccessCallback) = authDataSource.resetPassword(email,successCallback)

    override fun getUser(loginCallback: LoginCallback) {
        authDataSource.getUser { user, exception ->
            if (exception == null) {
                ioScope.launch {
                    val baseRequest = createBaseRequest(RequestType.GET_USER, authDataSource, user) ?: return@launch
                    val httpResponse = (networkManager.sendRequest(baseRequest) as? HttpResponse)
                    httpResponse?.let { response ->
                        val remoteUser = response.body<User>()
                        loginCallback(remoteUser,null)
                    } ?: run {
                        loginCallback(null,Exception())// check exception
                    }
                }
            } else loginCallback(null,exception)
        }
    }

    override fun createOrUpdateUser(user: User, loginCallback: LoginCallback) {
        ioScope.launch {
            val baseRequest = createBaseRequest(RequestType.CREATE_USER, authDataSource, user) ?: return@launch
            val httpResponse = (networkManager.sendRequest(baseRequest) as? HttpResponse)
            val success = httpResponse?.isSuccessful() == true
            if (success) loginCallback(user, null) else loginCallback(null, Exception())
        }
    }

    /** This is base request to to use in our network call architecture.
     * every class that implement AuthDataSource will demand here to fill the request */
    private fun createBaseRequest(
        requestType: RequestType,
        authDataSource: AuthDataSource,
        user: User?
    ): BaseRequest? {
        val baseRequest = when (authDataSource) {
            is FirebaseDataSource -> {
                when (requestType) {
                    RequestType.CREATE_USER -> {
                        if (user == null) return null
                        FirebaseUserRequests.CreateOrUpdateUser(body = user)
                    }
                    RequestType.GET_USER -> FirebaseUserRequests.GetUser()
                }
            }
        }
        return baseRequest
    }
}
