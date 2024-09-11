package com.example.composetemplate.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateUtil {

    companion object {
        const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"  //"2021-04-26T10:00:00Z"

        fun milliToDateFormat(dateMilli: Long, dateFormat: String = SERVER_DATE_FORMAT, gmt: Boolean = true): String? {
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            if (gmt) {
                simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            }
            return simpleDateFormat.format(Date(dateMilli))
        }
    }
}