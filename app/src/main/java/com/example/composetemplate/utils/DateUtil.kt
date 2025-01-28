package com.example.composetemplate.utils

import android.os.Build
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional
import kotlinx.datetime.toLocalDateTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtil {
    enum class Formatter {
        Backend, TitleShort, Hours, DayMonthShort, DataLongTitle;

        /**
         * Backend -> 2024-06-27T13:49:55.376Z
         * TitleShort -> "Apr 16, 2024"
         * Hours -> "14:44"
         * DayMonthShort -> "Tue 27 Jun"
         * DataLongTitle -> "27/06/2024"
         **/

        val format: DateTimeFormat<LocalDateTime>
            get() = LocalDateTime.Format {
                when (this@Formatter) {
                    Backend -> {
                        date(LocalDate.Formats.ISO)
                        char('T')
                        hour(); char(':'); minute()
                        optional {
                            char(':'); second()
                            optional {
                                char('.'); secondFraction(minLength = 3, maxLength = 3)
                            }
                            char('Z');
                        }
                    }

                    TitleShort -> {
                        monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); dayOfMonth(); chars(", "); year()
                    }

                    Hours -> {
                        hour(); char(':'); minute()
                    }

                    DayMonthShort -> {
                        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED); char(' '); dayOfMonth(); char(
                            ' '
                        ); monthName(MonthNames.ENGLISH_ABBREVIATED)
                    }

                    DataLongTitle -> {
                        dayOfMonth(); char('/'); monthNumber(); char('/'); year()
                    }
                }
            }

        val timeZone: kotlinx.datetime.TimeZone
            get() = when (this) {
                Backend -> kotlinx.datetime.TimeZone.UTC
                TitleShort, Hours, DayMonthShort, DataLongTitle -> kotlinx.datetime.TimeZone.currentSystemDefault()
            }
    }

    fun getCurrentDate(timeZone: kotlinx.datetime.TimeZone = kotlinx.datetime.TimeZone.UTC): LocalDateTime {
        return Clock.System.now().toLocalDateTime(timeZone)
    }

    fun getCurrentDateInMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }


    val currentTimeAsEpochSeconds: Long
        get() = Clock.System.now().epochSeconds

    fun convertMillisToLocalDateTime(selectedDateMillis: Long): LocalDateTime {

        val instant = Instant.fromEpochMilliseconds(selectedDateMillis)
        val date = instant.toLocalDateTime(kotlinx.datetime.TimeZone.UTC).date
        val currentTime =
            Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).time
        return LocalDateTime(
            year = date.year,
            month = date.month,
            dayOfMonth = date.dayOfMonth,
            hour = currentTime.hour,
            minute = currentTime.minute,
            second = currentTime.second,
            nanosecond = currentTime.nanosecond
        )
    }

}