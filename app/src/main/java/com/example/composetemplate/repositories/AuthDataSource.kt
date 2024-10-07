package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.data.remote.base.BaseRequest
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback

/** In this interface we set the all actions of auth and then we implement them in each service we want to use.
 * that Way we make our code more flexible and changeable and easily can change the all service we use
 * using the DI
 *
 * */

sealed interface AuthDataSource {
    fun createUserWithEmailAndPassword(user: User, password: String, loginCallback: LoginCallback)
    fun signInWithEmailAndPassword(user: User, password: String, loginCallback: LoginCallback)
    fun getUser(loginCallback: LoginCallback)
    fun resetPassword(email: String, successCallback: SuccessCallback)
    fun logout()
}