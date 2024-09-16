package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.LoginParameterizable
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.models.server_models.PermissionType
import com.example.composetemplate.data.remote.responses.ExampleResponse
import com.example.composetemplate.managers.NetworkManager
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback
import com.example.composetemplate.utils.extensions.isSuccessful
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * @Interactor explanation:
 * In MVVM, an Interactor encapsulates logic or actions that the ViewModel requests to process user interactions or data changes.
 * It acts as a middle layer between the ViewModel and repositories.
 * ensuring the ViewModel focuses only on managing UI-related data while the Interactor handles the core logic and data retrieval.
 */

/**
 *  @AuthInteractor PRE-CONDITION: enable every auth option in firebase console that you want to use,
 *  otherwise it will not work and the network calls will go to onFailure
 *  RECOMMENDATION: Add SHA1 key in firebase project settings, you need it for example, in google auth
 */

class AuthInteractor(
    private val loginRepository: LoginRepository,
    private val networkManager: NetworkManager
) {

    /** @LoginProvider make us to implement every auth type we would like to add*/
    enum class LoginProvider{
        REGISTER_WITH_EMAIL_AND_PASSWORD, SIGN_IN_WITH_EMAIL_AND_PASSWORD;
    }

    /**
     *  @login is a method with 1 purpose so we want to login we call login method
     * that's why every other login types methods are private and we call then according to @LoginProvider
     */
    fun login(loginProvider: LoginProvider, loginParams: LoginParameterizable, loginCallback: LoginCallback){
        when(loginProvider){
            LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD -> {
                val email = (loginParams as? NonSocialLoginParameter)?.email ?: ""
                val password = loginParams.password
                val user = User(email = email, fullName = loginParams.fullName, permissionType = PermissionType.DEVELOPER.name.lowercase())
                loginRepository.createEmailPasswordUser(user,password, loginCallback)
            }
            LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD -> {
                val email = (loginParams as? NonSocialLoginParameter)?.email ?: ""
                val password = loginParams.password
                val user = User(email = email, fullName = "", permissionType = PermissionType.DEVELOPER.name.lowercase())
                loginRepository.signInEmailPasswordUser(user,password, loginCallback)
            }
        }
    }

    /** Create or update in database */
    suspend fun createOrUpdateUserInDb(user: User): Boolean {
        val request = loginRepository.createOrUpdateUserRequest(user)
        val response = (networkManager.sendRequest(request) as? HttpResponse)
        return response?.isSuccessful() == true
    }

    fun getUserAccessToken(successCallback: SuccessCallback) =  loginRepository.getUserAccessToken(successCallback)

    /** Get user from database */
    suspend fun getUser(): User? {
        val request = loginRepository.getUserRequest()
        (networkManager.sendRequest(request) as? HttpResponse).let { response ->
           return response?.body<User>()
        }
    }

    fun logOut() = loginRepository.logOut()

}
