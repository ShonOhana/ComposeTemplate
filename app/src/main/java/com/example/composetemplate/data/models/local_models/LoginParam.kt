package com.example.composetemplate.data.models.local_models

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.SignInClient


/**
 * This File is for sending params according to each auth type
 * @sample NonSocialLoginParameter contain the parameters we need for email and password regular auth.
 *
 * In Google, Facebook, etc, we will need different parameters.
 *
 * For example:
 * Facebook -> we will need LoginResult that come from package com.facebook.login
 * OTP -> we will need ForceResendingToken that come from package com.google.firebase.auth
 *
 * every data class we will create for parameter need to implement LoginParameterizable
 * to use it in the main login() fun that need to get relevant parameters
 */
interface LoginParameterizable

data class NonSocialLoginParameter(
    val email: String,
    val password: String,
    val fullName: String = ""
): LoginParameterizable

data class GoogleAuthUiClientParameters(
    val context: Context,
    val oneTapClient: SignInClient,
    var intent: Intent? = null
): LoginParameterizable
