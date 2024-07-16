package com.example.composetemplate.presentation.screens.entry_screens.login

import com.example.composetemplate.utils.extensions.isValidEmail
import com.example.composetemplate.utils.extensions.isValidFullName
import com.example.composetemplate.utils.extensions.isValidPassword


data class LoginState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
) {

    val isFullNameValid: Boolean
        get() = fullName.isValidFullName()

    val isEmailValid: Boolean
        get() = email.isValidEmail()

    val isPasswordValid: Boolean
        get() = password.isValidPassword()

    val isConfirmPasswordValid: Boolean
        get() = password == confirmPassword

    val isValidLoginPage: Boolean
        get() = email.isValidEmail() && password.isValidPassword()

    val isValidRegisterPage: Boolean
        get() = isFullNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
}