package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.LoginParameterizable
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.models.server_models.PermissionType
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback

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
                loginRepository.registerUser(user,password, loginCallback)
            }
            LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD -> {
                val email = (loginParams as? NonSocialLoginParameter)?.email ?: ""
                val password = loginParams.password
                val user = User(email = email, fullName = "", permissionType = PermissionType.DEVELOPER.name.lowercase())
                loginRepository.signInEmailPasswordUser(user,password, loginCallback)
            }
        }
    }

    /** Get user from database */
    fun getUser(loginCallback: LoginCallback) = loginRepository.getUser(loginCallback)

    fun logOut() = loginRepository.logOut()

    fun resetPassword(email: String, successCallback: SuccessCallback) = loginRepository.resetPassword(email,successCallback)

}
