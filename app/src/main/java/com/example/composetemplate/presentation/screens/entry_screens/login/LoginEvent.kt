package com.example.composetemplate.presentation.screens.entry_screens.login

sealed class LoginEvent {

    data class UpdateEmail(val email: String): LoginEvent()

    data class UpdatePassword(val password: String): LoginEvent()

    data class UpdateFullName(val fullName: String): LoginEvent()

    data class UpdateConfirmPassword(val confirmPassword: String): LoginEvent()
}