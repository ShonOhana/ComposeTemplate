package com.example.composetemplate.utils

import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateUtil {

    companion object {
        const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"  //"2021-04-26T10:00:00Z"

        fun milliToDateFormat(
            dateMilli: Long,
            dateFormat: String = SERVER_DATE_FORMAT,
            gmt: Boolean = true
        ): String? {
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            if (gmt) {
                simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            }
            return simpleDateFormat.format(Date(dateMilli))
        }

        fun dateFormatToMilli(date: String, dateFormat: String = SERVER_DATE_FORMAT): Long {
            return try {
                val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
                simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
                val startDate = simpleDateFormat.parse(date)
                startDate?.time ?: 0L
            } catch (exception: ParseException) {
                0L
            }

        }

        fun lectureFormatDate(originalDate: String): String {
            // Define the input format
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC") // Set timezone to UTC
            }

            // Parse the original date string into a Date object
            val date: Date = inputFormat.parse(originalDate) ?: return ""

            // Define the output format
            val outputFormat = SimpleDateFormat("E dd MMM HH:mm", Locale.getDefault())

            // Format the Date to the desired output format
            return outputFormat.format(date)
        }
    }
}