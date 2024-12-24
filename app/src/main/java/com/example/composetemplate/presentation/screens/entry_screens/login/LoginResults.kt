package com.example.composetemplate.presentation.screens.entry_screens.login

import com.example.composetemplate.data.models.local_models.ErrorType
import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.errors.Errorable

sealed interface LoginResults

sealed interface SignUpResult : LoginResults {
    data class Success(val user: User?): SignUpResult
    data object Cancelled: SignUpResult
    data class Failure(val errorable: Errorable?): SignUpResult
}
sealed interface SignInResult :LoginResults {
    data class Success(val user: User?): SignInResult
    data object Cancelled: SignInResult
    data class Failure(val errorable: Errorable?): SignInResult
    data class NoCredentials(val errorable: Errorable?): SignInResult
}