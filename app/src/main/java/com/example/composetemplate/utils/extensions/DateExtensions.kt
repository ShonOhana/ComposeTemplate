package com.example.composetemplate.utils.extensions

import com.example.composetemplate.utils.DateUtil
import com.example.composetemplate.utils.DateUtil.currentTimeAsEpochSeconds
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.string(formatter: DateUtil.Formatter): String {
    val instant = this.toInstant(timeZone = TimeZone.UTC)
    return instant.toLocalDateTime(formatter.timeZone).format(formatter.format)
}

fun LocalDateTime.isPast() = DateUtil.convertMillisToLocalDateTime(currentTimeAsEpochSeconds) < this
