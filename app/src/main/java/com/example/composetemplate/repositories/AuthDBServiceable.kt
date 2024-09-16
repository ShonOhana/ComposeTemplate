package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.utils.LoginCallback
import com.example.composetemplate.utils.SuccessCallback

interface AuthDBServiceable {
    fun getUser(successCallback: SuccessCallback)
    fun createOrUpdateUser(user: User, loginCallback: LoginCallback)
}
