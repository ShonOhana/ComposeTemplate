package com.example.composetemplate.data.models.local_models


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
interface LoginParameterizable {
    val email: String
    val password: String
    val fullName: String
}

data class NonSocialLoginParameter(
    override val email: String,
    override val password: String,
    override val fullName: String
): LoginParameterizable
