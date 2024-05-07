package com.example.composetemplate.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

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


}