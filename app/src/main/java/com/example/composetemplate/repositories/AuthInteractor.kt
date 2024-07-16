package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.LoginParameterizable
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.models.server_models.PermissionType
import com.example.composetemplate.managers.NetworkManager
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.extensions.isSuccessful
import io.ktor.client.statement.HttpResponse

/**
 *  PRE-CONDITION: enable every auth option in firebase console that you want to use,
 *  otherwise it will not work and the network calls will go to onFailure
 *  RECOMMENDATION: Add SHA1 key in firebase project settings, you need it for example, in google auth
 */

class AuthInteractor(
    private val loginService: LoginRepository,
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
                val user = User(email = email, full_name = loginParams.fullName, permission_type = PermissionType.DEVELOPER.name.lowercase())
                loginService.createEmailPasswordUser(user,password, loginCallback)
            }
            LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD -> {
                val email = (loginParams as? NonSocialLoginParameter)?.email ?: ""
                val password = loginParams.password
                val user = User(email = email, full_name = "Shon", permission_type = PermissionType.DEVELOPER.name.lowercase())
                loginService.signInEmailPasswordUser(user,password, loginCallback)
            }
        }
    }

    /** Create or update in database */
    suspend fun createOrUpdateUserInDb(user: User): Boolean {
        val request = loginService.createOrUpdateUserRequest(user)
        val response = (networkManager.sendRequest(request) as? HttpResponse)
        return response?.isSuccessful() == true
    }

}
