package com.example.composetemplate.data.models.local_models

import com.example.composetemplate.utils.DateUtil
import kotlinx.serialization.Serializable

// here we don't save password security wise
@Serializable
data class User(
    var permission_type: String,
    var full_name: String,
    var created_on: String? = DateUtil.milliToDateFormat(System.currentTimeMillis()),
    var email: String,
    var fcm_token: String? = null,
)

