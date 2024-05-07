package com.example.composetemplate.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.composetemplate.presentation.login.LoginEvent
import com.example.composetemplate.presentation.login.LoginState

class RegisterViewModel: ViewModel() {

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


}