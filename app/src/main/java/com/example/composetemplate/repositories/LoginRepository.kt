package com.example.composetemplate.repositories

import com.example.composetemplate.data.local.CacheData.user
import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.data.remote.errors.APIError
import com.example.composetemplate.data.remote.errors.AuthError
import com.example.composetemplate.data.remote.requests.FirebaseUserRequests
import com.example.composetemplate.managers.MainNetworkManager
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginResults
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInResult
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpResult
import com.example.composetemplate.utils.extensions.errorType
import com.example.composetemplate.utils.extensions.isSuccessful
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

interface AuthDbServiceable {
    suspend fun createOrUpdateUser(user: User): Boolean
    suspend fun getUser(): LoginResults
}
/**
 *
 * In the MVVM architecture, the Repository layer acts as a mediator between the ViewModel and data sources.
 * It aggregates data from multiple sources, such as remote APIs, databases, and caches, and provides a clean API for the ViewModel
 * This class contain the functionality of all login auth types
 */
class LoginRepository(
    private val authActionable: AuthActionable,
    private val networkManager: MainNetworkManager,
): AuthDbServiceable {

    enum class RequestType {
        CREATE_USER, GET_USER
    }

    /** create user by email and password */
    suspend fun registerUser(user: User, password: String): SignUpResult {
        return try {
            when(val result = authActionable.createUserWithEmailAndPassword(user, password)) {
                SignUpResult.Cancelled -> SignUpResult.Cancelled
                is SignUpResult.Failure -> SignUpResult.Failure(result.errorable)
                is SignUpResult.Success -> {
                    createOrUpdateUser(user)
                    SignUpResult.Success(user)
                }
            }
        } catch (e: Exception) {
            SignUpResult.Failure(AuthError(e.errorType))
        }
    }

    /** sign in user by email and password */
    suspend fun signInEmailPasswordUser(user: User, password: String): SignInResult {
        return try {
            when(val result = authActionable.signInWithEmailAndPassword(user, password)) {
                is SignInResult.NoCredentials -> SignInResult.NoCredentials(result.errorable)
                SignInResult.Cancelled -> SignInResult.Cancelled
                is SignInResult.Failure -> SignInResult.Failure(result.errorable)
                is SignInResult.Success -> {
                    SignInResult.Success(user)
                }
            }
        } catch (e: Exception) {
            SignInResult.Failure(AuthError(e.errorType))
        }
    }

//    fun signInWithGoogle(activity: Activity,successCallback: SuccessCallback ){
//        ioScope.launch {
//            googleAuth.googleSignIn(activity, successCallback)
//        }
//    }

    fun logOut() = authActionable.logout()

    suspend fun resetPassword(email: String) = authActionable.resetPassword(email)

    override suspend fun getUser(): SignInResult {
        return try {
            when(val result = authActionable.getUser()) {
                SignInResult.Cancelled -> {
                    logOut()
                    SignInResult.Cancelled
                }
                is SignInResult.Failure -> {
                    logOut()
                    SignInResult.Failure(result.errorable)
                }
                is SignInResult.NoCredentials -> {
                    logOut()
                    SignInResult.NoCredentials(result.errorable)
                }
                is SignInResult.Success -> {
                    val baseRequest = createBaseRequest(RequestType.GET_USER, authActionable, user)
                        ?: return SignInResult.Failure(AuthError(ErrorType.UNKNOWN_ERROR)) //todo: check the error in httpResponse
                    val httpResponse = networkManager.sendRequest(baseRequest) as? HttpResponse
                    httpResponse?.let { response ->
                        val remoteUser = response.body<User>()
                        SignInResult.Success(remoteUser)
                    } ?: run {
                        logOut()
                        SignInResult.Failure(AuthError(ErrorType.UNKNOWN_ERROR)) //todo: check the error in httpResponse
                    }
                }
            }
        } catch (e: Exception) {
            SignInResult.Failure(AuthError(e.errorType))
        }
    }

    override suspend fun createOrUpdateUser(user: User): Boolean {
        val baseRequest = createBaseRequest(RequestType.CREATE_USER, authActionable, user) ?: return false
        return try {
            val httpResponse = networkManager.sendRequest(baseRequest) as? HttpResponse
            httpResponse?.isSuccessful() == true
        } catch (e: Exception) {
            false
        }
    }

    /** This is base request to to use in our network call architecture.
     * every class that implement AuthDataSource will demand here to fill the request */
    private fun createBaseRequest(
        requestType: RequestType,
        authActionable: AuthActionable,
        user: User?
    ): BaseRequest? {
        val baseRequest = when (authActionable) {
            is FirebaseActionable -> {
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
