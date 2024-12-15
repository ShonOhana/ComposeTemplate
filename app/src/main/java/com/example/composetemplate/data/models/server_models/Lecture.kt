package com.example.composetemplate.data.models.server_models

import com.example.composetemplate.utils.DateUtil
import com.example.composetemplate.utils.extensions.isPast
import com.example.composetemplate.utils.extensions.safeValueOfDate
import com.example.composetemplate.utils.extensions.string
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Lecture(
    @SerializedName("developer_id")
    val developerId: String,
    val topic: String,
    @SerializedName("developer_name")
    val developerName: String,
    @SerializedName("due_date")
    val dueDate: String
) {
    val isPast: Boolean
        get() = dueDate.safeValueOfDate()?.isPast() == true

    val dueDateString: String
        get() = dueDate.safeValueOfDate()?.string(DateUtil.Formatter.DayMonthShort) ?: ""
}
