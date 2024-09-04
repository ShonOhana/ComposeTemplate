package com.example.composetemplate.data.models.server_models

import com.google.gson.annotations.SerializedName

enum class PermissionType {
    @SerializedName("admin")
    ADMIN,
    @SerializedName("developer")
    DEVELOPER ;
}