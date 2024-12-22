package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.data.models.local_models.LoginParameterizable
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.models.server_models.PermissionType
import com.example.composetemplate.data.remote.errors.AuthError
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginResults
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpResult


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
        REGISTER_WITH_EMAIL_AND_PASSWORD, SIGN_IN_WITH_EMAIL_AND_PASSWORD , GOOGLE_SIGN_IN;
    }

    /**
     *  @login is a method with 1 purpose so we want to login we call login method
     * that's why every other login types methods are private and we call then according to @LoginProvider
     */
    suspend fun login(loginProvider: LoginProvider, loginParams: LoginParameterizable): LoginResults {
        when(loginProvider){
            LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD -> {
                val email = (loginParams as? NonSocialLoginParameter)?.email ?: ""
                val password = loginParams.password
                val user = User(email = email, fullName = loginParams.fullName, permissionType = PermissionType.DEVELOPER.name.lowercase())
                return loginRepository.registerUser(user,password)
            }
            LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD -> {
                val email = (loginParams as? NonSocialLoginParameter)?.email ?: ""
                val password = loginParams.password
                val user = User(email = email, fullName = "", permissionType = PermissionType.DEVELOPER.name.lowercase())
                return loginRepository.signInEmailPasswordUser(user,password)
            }
//
//            LoginProvider.GOOGLE_SIGN_IN -> {
//                loginRepository.signInWithGoogle(loginCallback)
//            }
            else -> {}
        }
        return SignUpResult.Failure(AuthError(ErrorType.UNKNOWN_ERROR)) //todo: fix
    }

    /** Get user from database */
    suspend fun getUser() = loginRepository.getUser()

    fun logOut() = loginRepository.logOut()

    suspend fun resetPassword(email: String) = loginRepository.resetPassword(email)




    //testtttt
//    suspend fun signUp(username: String, password: String) = accountManager.signUp(username, password)
//
//    suspend fun signIn() = accountManager.signIn()

}
