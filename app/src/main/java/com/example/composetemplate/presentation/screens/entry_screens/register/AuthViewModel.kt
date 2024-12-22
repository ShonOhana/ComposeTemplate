package com.example.composetemplate.presentation.screens.entry_screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.base.BaseViewModel
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
 * NOTE: In this class we use Firebase auth that work with old callback and does not support coroutines.
 * So in our auth functions here is open new coroutine to switch the dispatcher to main thread
 * because I could not use regular suspend fun that call inside vieModelScope because this old callbacks
 * not support coroutines
 * */
class AuthViewModel(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {

    var signInData by mutableStateOf(SignInData())
        private set
    var signupData by mutableStateOf(SignUpData())
        private set
    var forgotPasswordMail by mutableStateOf("")
        private set

    val signInResult = MutableStateFlow<SignInResult?>(null)

    fun fetchUserData(authScreenState: AuthScreenState?) {
        viewModelScope.launch {
            when (val signInResult = authInteractor.getUser()) {
                SignInResult.Cancelled -> setSignInResultState(
                    signInResult,
                    authScreenState,
                    null,
                    null
                )

                is SignInResult.Failure -> setSignInResultState(
                    signInResult,
                    authScreenState,
                    signInResult.errorable?.errorType?.messageKey,
                    null
                )

                is SignInResult.NoCredentials -> setSignInResultState(
                    signInResult,
                    authScreenState,
                    signInResult.errorable?.errorType?.messageKey,
                    null
                )

                is SignInResult.Success -> setSignInResultState(
                    signInResult,
                    authScreenState,
                    null,
                    signInResult.user
                )
            }
        }
    }

    fun signInEmailAndPassword() {
        viewModelScope.launch {
            uiState.value = UIState.Loading()
            val loginParams = NonSocialLoginParameter(signInData.email, signInData.password)
            val signInResult = authInteractor.login(
                LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD,
                loginParams
            ) as? SignInResult ?: return@launch

            when (signInResult) {
                SignInResult.Cancelled -> setSignInResultState(
                    signInResult,
                    AuthScreenState.Login,
                    null,
                    null
                )

                is SignInResult.Failure -> setSignInResultState(
                    signInResult,
                    AuthScreenState.Login,
                    signInResult.errorable?.errorType?.messageKey,
                    null
                )

                is SignInResult.NoCredentials -> setSignInResultState(
                    signInResult,
                    AuthScreenState.Login,
                    signInResult.errorable?.errorType?.messageKey,
                    null
                )

                is SignInResult.Success -> fetchUserData(AuthScreenState.Login)
            }
        }
    }

    fun createEmailPasswordUser() {
        viewModelScope.launch {
            uiState.value = UIState.Loading()
            val loginParams = NonSocialLoginParameter(
                email = signupData.email,
                password = signupData.password,
                fullName = signupData.fullName
            )
            val registerResult = authInteractor.login(
                LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD,
                loginParams
            ) as? SignUpResult ?: return@launch

            when (registerResult) {
                SignUpResult.Cancelled -> setSignInResultState(
                    SignInResult.Cancelled,
                    AuthScreenState.Register,
                    null,
                    null
                )

                is SignUpResult.Failure -> setSignInResultState(
                    SignInResult.Failure(registerResult.errorable),
                    AuthScreenState.Register,
                    registerResult.errorable?.errorType?.messageKey,
                    null
                )

                is SignUpResult.Success -> fetchUserData(AuthScreenState.Register)
            }
        }
    }

    /** Logout fun to use whenever we need to logout
     * NOTE: meanwhile I use it when I change loginState otherwise we would get permission denied
     * if we try to sign in or register if we already logged in.
     * */
    fun logOut() = authInteractor.logOut()

    fun resetPassword(email: String) {
        viewModelScope.launch {
            authInteractor.resetPassword(email)
        }
    }

    /** This is to set the text after the change in the edit text according to the parameter we change.
     * This method has direct connection to onEvent() method */
    fun setText(authTextFieldsEnum: AuthTextFieldsEnum, authScreenState: AuthScreenState): String {
        return when (authScreenState) {
            AuthScreenState.Login -> {
                when (authTextFieldsEnum) {
                    EMAIL -> signInData.email
                    PASSWORD -> signInData.password
                    FULL_NAME -> ""  //do noting in sign in
                    CONFIRM_PASSWORD -> ""  //do noting in sign in
                    AuthTextFieldsEnum.FORGOT_PASSWORD -> forgotPasswordMail
                }
            }

            AuthScreenState.Register -> {
                when (authTextFieldsEnum) {
                    EMAIL -> signupData.email
                    PASSWORD -> signupData.password
                    FULL_NAME -> signupData.fullName
                    CONFIRM_PASSWORD -> signupData.confirmPassword
                    AuthTextFieldsEnum.FORGOT_PASSWORD -> ""
                }
            }
        }
    }

    /** check the validation of the edit text according to the AuthTextFieldsEnum */
    fun validateEditText(
        authTextFieldsEnum: AuthTextFieldsEnum,
        authScreenState: AuthScreenState
    ): Boolean {
        return when (authScreenState) {
            AuthScreenState.Login -> {
                when (authTextFieldsEnum) {
                    AuthTextFieldsEnum.FORGOT_PASSWORD,
                    EMAIL -> signInData.isEmailValid

                    PASSWORD -> signInData.isPasswordValid
                    FULL_NAME -> false
                    CONFIRM_PASSWORD -> false
                }
            }

            AuthScreenState.Register -> {
                when (authTextFieldsEnum) {
                    AuthTextFieldsEnum.FORGOT_PASSWORD,
                    EMAIL -> signupData.isEmailValid

                    PASSWORD -> signupData.isPasswordValid
                    FULL_NAME -> signupData.isFullNameValid
                    CONFIRM_PASSWORD -> signupData.isConfirmPasswordValid
                }
            }
        }
    }

    /** Change the edit text value to its new value when you type the keyboard */
    fun onEvent(
        authTextFieldsEnum: AuthTextFieldsEnum,
        newValue: String,
        authScreenState: AuthScreenState
    ) {
        when (authScreenState) {
            AuthScreenState.Login -> {
                when (authTextFieldsEnum) {
                    EMAIL -> signInData = signInData.copy(email = newValue)
                    PASSWORD -> signInData = signInData.copy(password = newValue)
                    FULL_NAME -> {}
                    CONFIRM_PASSWORD -> {}
                    AuthTextFieldsEnum.FORGOT_PASSWORD -> forgotPasswordMail = newValue
                }
            }

            AuthScreenState.Register -> {
                when (authTextFieldsEnum) {
                    FULL_NAME -> signupData = signupData.copy(fullName = newValue)
                    EMAIL -> signupData = signupData.copy(email = newValue)
                    PASSWORD -> signupData = signupData.copy(password = newValue)
                    CONFIRM_PASSWORD -> signupData =
                        signupData.copy(confirmPassword = newValue)

                    AuthTextFieldsEnum.FORGOT_PASSWORD -> {}
                }
            }
        }
    }

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

//    suspend fun signUp() = authInteractor.signUp("Shon","123456")

}
