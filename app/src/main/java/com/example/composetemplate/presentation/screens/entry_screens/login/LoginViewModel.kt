package com.example.composetemplate.presentation.screens.entry_screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.utils.LoginProvider
import com.example.composetemplate.utils.LogsManager
import com.example.composetemplate.repositories.AuthInteractor
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthInteractor
): ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: LoginState by _loginState

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.UpdateEmail ->  {
                _loginState.value = loginState.copy(email = event.email)
            }
            is LoginEvent.UpdatePassword -> {
                _loginState.value = loginState.copy(password = event.password)
            }

            is LoginEvent.UpdateConfirmPassword -> { } // dont do nothing in login
            is LoginEvent.UpdateFullName -> { } // dont do nothing in login
        }
    }

    fun signInEmailAndPassword(){
        //todo: add state management ( Loading, success and Error in Flow)
        val loginParams = NonSocialLoginParameter(loginState.email,loginState.password, "Shon")// todo: remove shon after implement get user
        authRepository.login(LoginProvider.SIGN_IN_WITH_EMAIL_AND_PASSWORD,loginParams) { user, exception ->
            //todo: move to main screen or to error screen
            if (exception == null) {
                viewModelScope.launch {
                    if (user != null) {
                        val success = authRepository.createOrUpdateUserInDb(user)
                        if (success)
                            LogsManager().logMessage(
                                LogsManager.LogType.VERBOSE,
                                LogsManager.LogTag.GENERAL, "success")
                        else
                            LogsManager().logMessage(
                                LogsManager.LogType.VERBOSE,
                                LogsManager.LogTag.GENERAL, "Error")
                    }
                }
            }
        }
    }

}