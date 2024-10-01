package com.example.composetemplate.presentation.screens.entry_screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.CONFIRM_PASSWORD
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.EMAIL
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.FULL_NAME
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthTextFieldsEnum.PASSWORD
import com.example.composetemplate.presentation.screens.entry_screens.login.AuthScreenState
import com.example.composetemplate.presentation.screens.entry_screens.login.BaseAuthScreenData
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInData
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpData
import com.example.composetemplate.repositories.AuthInteractor
import com.example.composetemplate.utils.Constants
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.LoginProvider
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.utils.SuccessCallback
import com.example.composetemplate.utils.UIState
import com.example.composetemplate.utils.tag
import com.example.composetemplate.utils.type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception


class AuthViewModel(
    private val authInteractor: AuthInteractor
): ViewModel() {

    // set the uiState of the auth page
    private val uiAuthState = MutableStateFlow<UIState<Boolean>?>(null)

    //progress visibility according to uiState
    private val _isProgressVisible = mutableStateOf(false)
    val isProgressVisible: Boolean by _isProgressVisible

    //forgot password dialog visibility
    private val _isForgotDialogVisible = mutableStateOf(false)
    val isForgotDialogVisible: Boolean by _isForgotDialogVisible

    //authentication data parameters according to loginScreen statement
    private val _signInData = mutableStateOf(SignInData())
    val signInData: SignInData by _signInData
    private val _signupData = mutableStateOf(SignUpData())
    val signupData: SignUpData by _signupData
    private val _forgotPasswordMail = mutableStateOf("")
    val forgotPasswordMail: String by _forgotPasswordMail


    init {
        initPageState()
    }

    /** This is the flow that set the UI according to the data */
    private fun initPageState() {
        viewModelScope.launch {
            uiAuthState.collect { state ->
                if (state != null) {
                    when (state) {
                        is UIState.Error -> {
                            LogsManager().logMessage(
                                type.VERBOSE,
                                tag.GENERAL,
                                state.message ?: Constants.UNKNOWN_EXCEPTION
                            )
                            _isProgressVisible.value = false
                        }

                        is UIState.Loading -> {
                            _isProgressVisible.value = true
                        }

                        is UIState.Success -> {
                            _isProgressVisible.value = false
                        }
                    }
                }
            }
        }
    }

    //todo interact with the interactor only once ( you call authInteractor twice)
    fun createEmailPasswordUser(successCallback: SuccessCallback) {
        uiAuthState.value = UIState.Loading()
        val loginParams =
            NonSocialLoginParameter(signupData.email, signupData.password, signupData.fullName)
        authInteractor.login(
            LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD,
            loginParams
        ) { user, exception ->
            if (exception == null) {
                viewModelScope.launch {
                    if (user != null) {
                        val success = authInteractor.createOrUpdateUserInDb(user)
                        if (success){
                            _signupData.value = signupData.copy(authError = false)
                            uiAuthState.value = UIState.Success(true)
                            successCallback(true,null)
                        }else {
                            uiAuthState.value = UIState.Error(Constants.UNKNOWN_EXCEPTION)
                            _signupData.value = signupData.copy(authError = true)
                            successCallback(false, Exception(Constants.UNKNOWN_EXCEPTION))
                        }
                    }
                }
            } else {
                uiAuthState.value = UIState.Error(exception.message)
                _signupData.value = signupData.copy(authError = true)
                successCallback(false, exception)
            }
        }
    }

    fun getUser(loginCallback: LoginCallback) {
        authInteractor.getUserAccessToken { success, exception ->
            if (success && exception == null) {
                viewModelScope.launch {
                    val user = authInteractor.getUser()
                    loginCallback(user, null)
                }
            } else loginCallback(null , exception)
        }
    }

    fun signInEmailAndPassword(successCallback: SuccessCallback){
        uiAuthState.value = UIState.Loading()
        val loginParams = NonSocialLoginParameter(signInData.email,signInData.password)
        authInteractor.login(LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD,loginParams) { user, exception ->
            if (exception == null) {
                _signInData.value = signInData.copy(authError = false)
                viewModelScope.launch {
                    if (user != null) {
                        val success = authInteractor.createOrUpdateUserInDb(user)
                        if (success){
                            _signInData.value = signInData.copy(authError = false)
                            uiAuthState.value = UIState.Success(true)
                            successCallback(true,null)
                        }else {
                            uiAuthState.value = UIState.Error(Constants.UNKNOWN_EXCEPTION)
                            _signInData.value = signInData.copy(authError = true)
                            successCallback(false, Exception(Constants.UNKNOWN_EXCEPTION))
                        }
                    }
                }
            } else {
                uiAuthState.value = UIState.Error(exception.message)
                _signInData.value = signInData.copy(authError = true)
                successCallback(false, exception)
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
    fun validateEditText(authTextFieldsEnum: AuthTextFieldsEnum, authScreenState: AuthScreenState): Boolean {
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
    fun onEvent(authTextFieldsEnum: AuthTextFieldsEnum, newValue: String, authScreenState: AuthScreenState){
        when (authScreenState) {
            AuthScreenState.Login -> {
                when (authTextFieldsEnum) {
                    EMAIL -> _signInData.value = signInData.copy(email = newValue)
                    PASSWORD -> _signInData.value = signInData.copy(password = newValue)
                    FULL_NAME -> { }
                    CONFIRM_PASSWORD -> { }
                    AuthTextFieldsEnum.FORGOT_PASSWORD -> _forgotPasswordMail.value = newValue
                }
            }
            AuthScreenState.Register -> {
                when (authTextFieldsEnum) {
                    FULL_NAME -> _signupData.value = signupData.copy(fullName = newValue)
                    EMAIL -> _signupData.value = signupData.copy(email = newValue)
                    PASSWORD -> _signupData.value = signupData.copy(password = newValue)
                    CONFIRM_PASSWORD -> _signupData.value = signupData.copy(confirmPassword = newValue)
                    AuthTextFieldsEnum.FORGOT_PASSWORD -> {}
                }
            }
        }
    }

    fun setForgotDialogVisibility(isVisible: Boolean) {
        _isForgotDialogVisible.value = isVisible
    }

    /** Logout fun to use whenever we need to logout
     * NOTE: meanwhile I use it when I change loginState otherwise we would get permission denied
     * if we try to sign in or register if we already logged in.
     * */
    fun logOut() = authInteractor.logOut()

    fun resetPassword(email: String) {
        uiAuthState.value = UIState.Loading()
        authInteractor.resetPassword(email) { success, exception ->
            if (success && exception == null)
                uiAuthState.value = UIState.Success(true)
            else
                uiAuthState.value = UIState.Error(exception?.message)
        }
    }
}