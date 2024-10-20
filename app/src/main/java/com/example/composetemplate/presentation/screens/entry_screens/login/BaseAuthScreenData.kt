package com.example.composetemplate.presentation.screens.entry_screens.login

import com.example.composetemplate.utils.extensions.isValidEmail
import com.example.composetemplate.utils.extensions.isValidFullName
import com.example.composetemplate.utils.extensions.isValidPassword


interface BaseAuthScreenData {
    val email: String
    val password: String
    val isEmailValid: Boolean
        get() = email.isValidEmail()

    val isPasswordValid: Boolean
        get() = password.isValidPassword()
}

data class SignInData(
    override val email: String = "",
    override val password: String = ""
) : BaseAuthScreenData {
    val isValidLoginPage: Boolean
        get() = email.isValidEmail() && password.isValidPassword()
}

data class SignUpData(
    val fullName: String = "",
    override val email: String = "",
    override val password: String = "",
    val confirmPassword: String = "",
) : BaseAuthScreenData {

    val isFullNameValid: Boolean
        get() = fullName.isValidFullName()

    val isConfirmPasswordValid: Boolean
        get() = password == confirmPassword

    val isValidRegisterPage: Boolean
        get() = isFullNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
}
