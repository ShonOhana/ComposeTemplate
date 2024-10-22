package com.example.composetemplate.data.models.server_models

import com.example.composetemplate.utils.DateUtil

data class Lecture(
    val title: String,
    val author: String,
    val timeStamp: String
) {
    val isPast: Boolean
        get() = System.currentTimeMillis() > DateUtil.dateFormatToMilli(timeStamp)
}
