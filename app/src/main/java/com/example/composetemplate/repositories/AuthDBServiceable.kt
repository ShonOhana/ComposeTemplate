package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback

interface AuthDBServiceable {
    /** Network call so i want it to be suspend and manage the threads */
    suspend fun getUser(loginCallback: LoginCallback)
    suspend fun createOrUpdateUser(user: User, loginCallback: LoginCallback)
}
