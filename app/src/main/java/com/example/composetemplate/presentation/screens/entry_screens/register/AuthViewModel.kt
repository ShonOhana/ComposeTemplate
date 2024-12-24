package com.example.composetemplate.presentation.screens.entry_screens.register

import android.content.IntentSender
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.base.BaseViewModel
import com.example.composetemplate.data.models.local_models.GoogleAuthUiClientParameters
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.CONFIRM_PASSWORD
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.EMAIL
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.FULL_NAME
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.PASSWORD
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInData
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInResult
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpData
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpResult
import com.example.composetemplate.repositories.AuthInteractor
import com.example.composetemplate.utils.LoginProvider
import com.example.composetemplate.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
/**
 * ViewModel class responsible for handling authentication-related logic.
 *
 * This class interacts with the `AuthInteractor` to perform various authentication operations
 * such as sign-in, registration, password reset, and user data fetching. Since Firebase authentication
 * uses old callback-based APIs, coroutines are used to switch the dispatcher to the main thread,
 * enabling safe and efficient interaction with the UI layer.
 *
 * @constructor
 * @param authInteractor The interactor used to perform authentication-related operations.
 */
class AuthViewModel(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {

    /** Data used for sign-in operations. */
    var signInData by mutableStateOf(SignInData())
        private set

    /** Data used for sign-up operations. */
    var signupData by mutableStateOf(SignUpData())
        private set

    /** Stores the email address for the forgot password operation. */
    var forgotPasswordMail by mutableStateOf("")
        private set

    /** Holds the result of the sign-in operation. */
    val signInResult = MutableStateFlow<SignInResult?>(null)

    /**
     * Fetches user data and updates the authentication state based on the result.
     *
     * @param authScreenState The current authentication screen state (Login or Register).
     */
    fun fetchUserData(authScreenState: AuthScreenState?) {
        viewModelScope.launch {
            when (val result = authInteractor.getUser()) {
                SignInResult.Cancelled -> setSignInResultState(result, authScreenState, null, null)
                is SignInResult.Failure -> setSignInResultState(result, authScreenState, result.errorable?.errorType?.messageKey, null)
                is SignInResult.NoCredentials -> setSignInResultState(result, authScreenState, result.errorable?.errorType?.messageKey, null)
                is SignInResult.Success -> setSignInResultState(result, authScreenState, null, result.user)
            }
        }
    }

    /**
     * Signs in the user using email and password.
     */
    fun signInEmailAndPassword() {
        viewModelScope.launch {
            uiState.value = UIState.Loading()
            val loginParams = NonSocialLoginParameter(signInData.email, signInData.password)
            val result = authInteractor.login(LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD, loginParams) as? SignInResult ?: return@launch
            when (result) {
                SignInResult.Cancelled -> setSignInResultState(result, AuthScreenState.Login, null, null)
                is SignInResult.Failure -> setSignInResultState(result, AuthScreenState.Login, result.errorable?.errorType?.messageKey, null)
                is SignInResult.NoCredentials -> setSignInResultState(result, AuthScreenState.Login, result.errorable?.errorType?.messageKey, null)
                is SignInResult.Success -> fetchUserData(AuthScreenState.Login)
            }
        }
    }

    /**
     * Registers a new user using email, password, and full name.
     */
    fun createEmailPasswordUser() {
        viewModelScope.launch {
            uiState.value = UIState.Loading()
            val registerParams = NonSocialLoginParameter(signupData.email, signupData.password, signupData.fullName)
            val result = authInteractor.login(LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD, registerParams) as? SignUpResult ?: return@launch
            when (result) {
                SignUpResult.Cancelled -> setSignInResultState(SignInResult.Cancelled, AuthScreenState.Register, null, null)
                is SignUpResult.Failure -> setSignInResultState(SignInResult.Failure(result.errorable), AuthScreenState.Register, result.errorable?.errorType?.messageKey, null)
                is SignUpResult.Success -> fetchUserData(AuthScreenState.Register)
            }
        }
    }

    /**
     * Signs in a user with their Google account.
     *
     * @param googleAuthUiClient The parameters required for Google Sign-In authentication.
     */
    fun signInWithGoogle(googleAuthUiClient: GoogleAuthUiClientParameters) {
        viewModelScope.launch {
            val result = authInteractor.login(LoginProvider.GOOGLE_SIGN_IN, googleAuthUiClient) as? SignInResult ?: return@launch
            when (result) {
                SignInResult.Cancelled -> setSignInResultState(result, AuthScreenState.Login, null, null)
                is SignInResult.Failure -> setSignInResultState(result, AuthScreenState.Login, result.errorable?.errorType?.messageKey, null)
                is SignInResult.NoCredentials -> setSignInResultState(result, AuthScreenState.Login, result.errorable?.errorType?.messageKey, null)
                is SignInResult.Success -> setSignInResultState(result, null, null, result.user)
            }
        }
    }

    /**
     * Creates an IntentSender for Google Sign-In.
     *
     * This function generates an `IntentSender` to facilitate the Google Sign-In process and open auth dialog.
     *
     * @param googleAuthUiClient The parameters required for Google Sign-In.
     * @return The `IntentSender` object used for launching the Google Sign-In UI.
     */
    suspend fun openGoogleAuthDialog(googleAuthUiClient: GoogleAuthUiClientParameters) =
        authInteractor.openGoogleAuthDialog(googleAuthUiClient)

    /**
     * Logs out the current user.
     */
    fun logOut() = authInteractor.logOut()

    /**
     * Resets the password for the given email address.
     *
     * @param email The email address to send the reset password link to.
     */
    fun resetPassword(email: String) {
        viewModelScope.launch {
            authInteractor.resetPassword(email)
        }
    }

    /**
     * Returns the current text value for a specific input field based on the authentication screen state.
     *
     * @param authTextFieldsEnum The enum value representing the input field.
     * @param authScreenState The current authentication screen state.
     * @return The current text value for the input field.
     */
    fun setText(authTextFieldsEnum: AuthTextFieldsEnum, authScreenState: AuthScreenState): String {
        return when (authScreenState) {
            AuthScreenState.Login -> when (authTextFieldsEnum) {
                EMAIL -> signInData.email
                PASSWORD -> signInData.password
                FULL_NAME, CONFIRM_PASSWORD -> ""
                AuthTextFieldsEnum.FORGOT_PASSWORD -> forgotPasswordMail
            }
            AuthScreenState.Register -> when (authTextFieldsEnum) {
                EMAIL -> signupData.email
                PASSWORD -> signupData.password
                FULL_NAME -> signupData.fullName
                CONFIRM_PASSWORD -> signupData.confirmPassword
                AuthTextFieldsEnum.FORGOT_PASSWORD -> ""
            }
        }
    }

    /**
     * Validates the input for a specific field based on the authentication screen state.
     *
     * @param authTextFieldsEnum The enum value representing the input field.
     * @param authScreenState The current authentication screen state.
     * @return `true` if the input is valid, otherwise `false`.
     */
    fun validateEditText(authTextFieldsEnum: AuthTextFieldsEnum, authScreenState: AuthScreenState): Boolean {
        return when (authScreenState) {
            AuthScreenState.Login -> when (authTextFieldsEnum) {
                EMAIL -> signInData.isEmailValid
                PASSWORD -> signInData.isPasswordValid
                else -> false
            }
            AuthScreenState.Register -> when (authTextFieldsEnum) {
                EMAIL -> signupData.isEmailValid
                PASSWORD -> signupData.isPasswordValid
                FULL_NAME -> signupData.isFullNameValid
                CONFIRM_PASSWORD -> signupData.isConfirmPasswordValid
                else -> false
            }
        }
    }

    /**
     * Updates the text value for a specific input field based on the authentication screen state.
     *
     * @param authTextFieldsEnum The enum value representing the input field.
     * @param newValue The new value to set.
     * @param authScreenState The current authentication screen state.
     */
    fun onEvent(authTextFieldsEnum: AuthTextFieldsEnum, newValue: String, authScreenState: AuthScreenState) {
        when (authScreenState) {
            AuthScreenState.Login -> when (authTextFieldsEnum) {
                EMAIL -> signInData = signInData.copy(email = newValue)
                PASSWORD -> signInData = signInData.copy(password = newValue)
                AuthTextFieldsEnum.FORGOT_PASSWORD -> forgotPasswordMail = newValue
                else -> {}
            }
            AuthScreenState.Register -> when (authTextFieldsEnum) {
                FULL_NAME -> signupData = signupData.copy(fullName = newValue)
                EMAIL -> signupData = signupData.copy(email = newValue)
                PASSWORD -> signupData = signupData.copy(password = newValue)
                CONFIRM_PASSWORD -> signupData = signupData.copy(confirmPassword = newValue)
                else -> {}
            }
        }
    }

    /**
     * Updates the UI state and sign-in result state based on the authentication result.
     *
     * @param signInResult The sign-in result.
     * @param authScreenState The current authentication screen state.
     * @param errorMessageKey The error message key, if any.
     * @param user The user object, if any.
     */
    private fun setSignInResultState(
        signInResult: SignInResult,
        authScreenState: AuthScreenState?,
        errorMessageKey: String?,
        user: User?,
    ) {
        if (user != null) uiState.value = UIState.Success(user)
        else uiState.value = UIState.Error(errorMessageKey)

        this.signInResult.value = signInResult

        authScreenState ?: return
        when (authScreenState) {
            AuthScreenState.Login -> signInData.authError = errorMessageKey
            AuthScreenState.Register -> signupData.authError = errorMessageKey
        }
    }
}
