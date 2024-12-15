package com.example.composetemplate.presentation.screens.entry_screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.base.BaseViewModel
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.CONFIRM_PASSWORD
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.EMAIL
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.FULL_NAME
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.PASSWORD
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInData
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpData
import com.example.composetemplate.repositories.AuthInteractor
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.LoginProvider
import com.example.composetemplate.utils.SuccessCallback
import com.example.composetemplate.utils.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.logging.ErrorManager

/**
 * NOTE: In this class we use Firebase auth that work with old callback and does not support coroutines.
 * So in our auth functions here is open new coroutine to switch the dispatcher to main thread
 * because I could not use regular suspend fun that call inside vieModelScope because this old callbacks
 * not support coroutines
 * */
class AuthViewModel(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {

    //authentication data parameters according to loginScreen statement
    private val _signInData = mutableStateOf(SignInData())
    val signInData: SignInData by _signInData
    private val _signupData = mutableStateOf(SignUpData())
    val signupData: SignUpData by _signupData
    private val _forgotPasswordMail = mutableStateOf("")
    val forgotPasswordMail: String by _forgotPasswordMail

    init {
        getUser()
    }

    fun createEmailPasswordUser(successCallback: SuccessCallback) {
        viewModelScope.launch {
            uiState.value = UIState.Loading()
            val loginParams =
                NonSocialLoginParameter(signupData.email, signupData.password, signupData.fullName)
            // Get data from the repository (IO scope)
            authInteractor.login(
                LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD,
                loginParams
            ) { user, exception ->
                viewModelScope.launch {
                    // Switch to Main dispatcher to update the UI
                    withContext(Dispatchers.Main) {
                        if (user != null && exception == null) {
                            getUser()
                            _signupData.value = signupData.copy(authError = null)
                            successCallback(true, null)
                        } else {
                            uiState.value = UIState.Error(exception?.message)
                            _signupData.value = signupData.copy(authError = exception?.message)
                            successCallback(false, exception)
                        }
                    }
                }
            }
        }
    }

    private fun getUser() {
        authInteractor.getUser { user, exception ->
            user?.let {
                uiState.value = UIState.Success(user)
            } ?: run {
                uiState.value = UIState.Error(exception?.message)
            }
        }
    }

    fun signInEmailAndPassword(successCallback: SuccessCallback) {
        uiState.value = UIState.Loading()
        val loginParams = NonSocialLoginParameter(signInData.email, signInData.password)
        // Get data from the repository (IO scope)
        authInteractor.login(
            LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD,
            loginParams
        ) { user, exception ->
            viewModelScope.launch {
                // Switch to Main dispatcher to update the UI
                withContext(Dispatchers.Main) {
                    if (user != null && exception == null) {
                        getUser()
                        _signInData.value = signInData.copy(authError = null)
                        successCallback(true, null)
                    } else {
                        uiState.value = UIState.Error(exception?.message)
                        _signInData.value = signInData.copy(authError = exception?.message)
                        successCallback(false, exception)
                    }
                }
            }
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
                    EMAIL -> _signInData.value = signInData.copy(email = newValue)
                    PASSWORD -> _signInData.value = signInData.copy(password = newValue)
                    FULL_NAME -> {}
                    CONFIRM_PASSWORD -> {}
                    AuthTextFieldsEnum.FORGOT_PASSWORD -> _forgotPasswordMail.value = newValue
                }
            }

            AuthScreenState.Register -> {
                when (authTextFieldsEnum) {
                    FULL_NAME -> _signupData.value = signupData.copy(fullName = newValue)
                    EMAIL -> _signupData.value = signupData.copy(email = newValue)
                    PASSWORD -> _signupData.value = signupData.copy(password = newValue)
                    CONFIRM_PASSWORD -> _signupData.value =
                        signupData.copy(confirmPassword = newValue)

                    AuthTextFieldsEnum.FORGOT_PASSWORD -> {}
                }
            }
        }
    }

    /** Logout fun to use whenever we need to logout
     * NOTE: meanwhile I use it when I change loginState otherwise we would get permission denied
     * if we try to sign in or register if we already logged in.
     * */
    fun logOut() = authInteractor.logOut()

    fun resetPassword(email: String) {
        uiState.value = UIState.Loading()
        authInteractor.resetPassword(email) { success, exception ->
            if (success && exception == null)
                uiState.value = UIState.Success(true)
            else
                uiState.value = UIState.Error(exception?.message)
        }
    }
}
