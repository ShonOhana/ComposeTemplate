package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.GoogleCredentialAuthParameter
import com.example.composetemplate.data.models.local_models.LoginParameterizable
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.models.server_models.PermissionType
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginResults
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInResult


/**
 * @AuthInteractor
 * In the MVVM architecture, the AuthInteractor acts as the middle layer between the ViewModel
 * and repositories, handling authentication logic. This allows the ViewModel to focus solely
 * on managing UI-related data while delegating authentication-related operations to this interactor.
 *
 * PRE-CONDITION:
 * - Enable the desired authentication options in the Firebase console before use.
 * - Add the SHA1 key in your Firebase project settings for specific authentication types (e.g., Google Sign-In).
 *
 * RECOMMENDATION:
 * - Ensure all required settings in the Firebase console are configured to avoid runtime failures.
 */
class AuthInteractor(
    private val loginRepository: LoginRepository
) {

    /**
     * @LoginProvider
     * Enum to represent supported login methods.
     */
    enum class LoginProvider {
        REGISTER_WITH_EMAIL_AND_PASSWORD,
        SIGN_IN_WITH_EMAIL_AND_PASSWORD,
        GOOGLE_SIGN_IN
    }

    /**
     * Handles the login process based on the specified [LoginProvider].
     *
     * @param loginProvider Specifies the authentication method to use.
     * @param loginParams Encapsulates parameters required for the authentication process.
     * @return A [LoginResults] object representing the result of the login operation.
     */
    suspend fun login(loginProvider: LoginProvider, loginParams: LoginParameterizable): LoginResults {
        return when (loginProvider) {
            LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD -> {
                val params = (loginParams as? NonSocialLoginParameter) ?: return SignInResult.Cancelled
                val email = params.email
                val password = params.password
                val user = User(
                    email = email,
                    fullName = params.fullName,
                    permissionType = PermissionType.DEVELOPER.name.lowercase()
                )
                loginRepository.registerUser(user, password)
            }

            LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD -> {
                val params = (loginParams as? NonSocialLoginParameter) ?: return SignInResult.Cancelled
                val email = params.email
                val password = params.password
                val user = User(
                    email = email,
                    fullName = "",
                    permissionType = PermissionType.DEVELOPER.name.lowercase()
                )
                loginRepository.signInEmailPasswordUser(user, password)
            }
             LoginProvider.GOOGLE_SIGN_IN -> {
                 val params = (loginParams as? GoogleCredentialAuthParameter) ?: return SignInResult.Cancelled
                 loginRepository.googleSignIn(params)
             }
        }
    }

    /**
     * Retrieves the currently authenticated user from the repository.
     *
     * @return A [LoginResults] object representing the user or an error state.
     */
    suspend fun getUser() = loginRepository.getUser()

    /**
     * Logs out the currently authenticated user by delegating to the repository.
     */
    fun logOut() = loginRepository.logOut()

    /**
     * Initiates a password reset for the given email address.
     *
     * @param email The email address to reset the password for.
     */
    suspend fun resetPassword(email: String) = loginRepository.resetPassword(email)

}
