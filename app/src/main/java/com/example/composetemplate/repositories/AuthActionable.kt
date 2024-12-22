package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.presentation.screens.entry_screens.login.SignInResult
import com.example.composetemplate.presentation.screens.entry_screens.login.SignUpResult

/** In this interface we set the all actions of auth and then we implement them in each service we want to use.
 * that Way we make our code more flexible and changeable and easily can change the all service we use
 * using the DI
 *
 * */

sealed interface AuthActionable {
    suspend fun createUserWithEmailAndPassword(user: User, password: String) : SignUpResult
    suspend fun signInWithEmailAndPassword(user: User, password: String): SignInResult
    suspend fun getUser() : SignInResult
    suspend fun resetPassword(email: String)
    fun logout()
}