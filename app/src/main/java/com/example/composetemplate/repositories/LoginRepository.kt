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

/**
 * Interface for AuthDbServiceable defines methods related to user creation and retrieval.
 */
interface AuthDbServiceable {
    suspend fun createOrUpdateUser(user: User): Boolean
    suspend fun getUser(): LoginResults
}

/**
 * The LoginRepository is responsible for handling authentication logic.
 * It serves as a mediator between different data sources (e.g., Firebase, local cache, network) and the ViewModel.
 *
 * It performs operations like user registration, sign-in, password reset, and handling user data.
 */
class LoginRepository(
    private val authActionable: AuthActionable,
    private val networkManager: MainNetworkManager
) : AuthDbServiceable {

    // Enum to define the request types for user operations
    enum class RequestType {
        CREATE_USER, GET_USER
    }

    /**
     * Registers a new user with the provided email and password.
     *
     * @param user The [User] object containing user details.
     * @param password The password for the user.
     * @return The result of the sign-up operation, which can either be success or failure.
     */
    suspend fun registerUser(user: User, password: String): SignUpResult {
        return try {
            when (val result = authActionable.createUserWithEmailAndPassword(user, password)) {
                /* Handle cancellation or failure cases */
                SignUpResult.Cancelled, is SignUpResult.Failure -> result
                /* On successful creation, update the user in the local database */
                is SignUpResult.Success -> {
                    createOrUpdateUser(user)
                    result
                }
            }
        } catch (e: Exception) {
            /* Return failure result on exception */
            SignUpResult.Failure(AuthError(e.errorType))
        }
    }

    /**
     * Signs in a user using the provided email and password.
     *
     * @param user The [User] object containing user details.
     * @param password The password for the user.
     * @return The result of the sign-in operation.
     */
    suspend fun signInEmailPasswordUser(user: User, password: String) = authActionable.signInWithEmailAndPassword(user, password)

    /**
     * Logs out the current user by invoking the logout functionality in [authActionable].
     */
    fun logOut() = authActionable.logout()

    /**
     * Initiates a password reset for the user with the provided email.
     *
     * @param email The email of the user for whom the password needs to be reset.
     */
    suspend fun resetPassword(email: String) = authActionable.resetPassword(email)

    /**
     * Retrieves the current user from the local cache or database, and updates it by making a network call if necessary.
     *
     * @return The result of the sign-in operation, which can either be success, failure, or no credentials.
     */
    override suspend fun getUser(): SignInResult {
        return try {
            when (val result = authActionable.getUser()) {
                /* Handle failure or cancelled cases */
                SignInResult.Cancelled, is SignInResult.Failure, is SignInResult.NoCredentials -> {
                    logOut()
                    result
                }
                /* On success, make a network request to fetch the user data */
                is SignInResult.Success -> {
                    val baseRequest = createBaseRequest(RequestType.GET_USER, authActionable, user) ?: return result
                    val httpResponse = networkManager.sendRequest(baseRequest) as? HttpResponse
                    httpResponse?.let { response ->
                        val remoteUser = response.body<User>()
                        SignInResult.Success(remoteUser)
                    } ?: run {
                        logOut()
                        result
                    }
                }
            }
        } catch (e: Exception) {
            /* Return failure result on exception */
            SignInResult.Failure(AuthError(e.errorType))
        }
    }

    /**
     * Creates or updates the user in the local database.
     *
     * @param user The [User] object containing user details.
     * @return True if the user was successfully created or updated, false otherwise.
     */
    override suspend fun createOrUpdateUser(user: User): Boolean {
        val baseRequest = createBaseRequest(RequestType.CREATE_USER, authActionable, user) ?: return false
        return try {
            val httpResponse = networkManager.sendRequest(baseRequest) as? HttpResponse
            httpResponse?.isSuccessful() == true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Creates a base request for user operations (create or get user).
     *
     * @param requestType The type of request to create (create or get user).
     * @param authActionable The object responsible for handling authentication actions.
     * @param user The user data to be used in the request.
     * @return A [BaseRequest] that can be used in network requests.
     */
    private fun createBaseRequest(
        requestType: RequestType,
        authActionable: AuthActionable,
        user: User?
    ): BaseRequest? {
        return when (authActionable) {
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
    }
}
