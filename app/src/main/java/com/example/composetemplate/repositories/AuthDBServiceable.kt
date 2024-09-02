package com.example.composetemplate.repositories

import com.example.composetemplate.data.models.local_models.User
import com.example.composetemplate.utils.LoginCallback

interface AuthDBServiceable {
    fun getUser(loginCallback: LoginCallback)
    fun createOrUpdateUserInDB(user: User, loginCallback: LoginCallback)
}
