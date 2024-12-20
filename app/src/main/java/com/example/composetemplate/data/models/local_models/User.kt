package com.example.composetemplate.data.models.local_models

import com.example.composetemplate.utils.DateUtil
import com.example.composetemplate.utils.extensions.string
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

// here we don't save password security wise
@Serializable
data class User(
    var permissionType: String,
    var fullName: String,
    var createdOn: String? = DateUtil.convertMillisToLocalDateTime(System.currentTimeMillis()).string(DateUtil.Formatter.Backend),
    var email: String,
    var fcm_token: String? = null,
)

