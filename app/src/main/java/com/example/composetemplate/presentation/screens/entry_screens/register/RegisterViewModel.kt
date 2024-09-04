package com.example.composetemplate.presentation.screens.entry_screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composetemplate.data.models.local_models.NonSocialLoginParameter
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginEvent
import com.example.composetemplate.presentation.screens.entry_screens.login.LoginState
import com.example.composetemplate.repositories.AuthInteractor
import com.example.composetemplate.utils.LoginProvider
import com.example.composetemplate.utils.LogsManager
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authInteractor: AuthInteractor
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

            is LoginEvent.UpdateConfirmPassword -> {
                _loginState.value = loginState.copy(confirmPassword = event.confirmPassword)
            }
            is LoginEvent.UpdateFullName -> {
                _loginState.value = loginState.copy(fullName = event.fullName)
            }
        }
    }

    fun createEmailPasswordUser() {
        //todo: add state management ( Loading, success and Error in Flow)
        val loginParams =
            NonSocialLoginParameter(loginState.email, loginState.password, loginState.fullName)
        authInteractor.login(
            LoginProvider.REGISTER_WITH_EMAIL_AND_PASSWORD,
            loginParams
        ) { user, exception ->
            //todo: move to main screen or to error screen
            if (exception == null) {
                viewModelScope.launch {
                    if (user != null) {
                        val success = authInteractor.createOrUpdateUserInDb(user)
                        if (success)
                            LogsManager().logMessage(LogsManager.LogType.VERBOSE,LogsManager.LogTag.GENERAL, "success")
                        else
                            LogsManager().logMessage(LogsManager.LogType.VERBOSE,LogsManager.LogTag.GENERAL, "Error")
                    }
                }
            }
        }
    }
}