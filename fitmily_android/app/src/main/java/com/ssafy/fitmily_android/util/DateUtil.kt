package com.ssafy.fitmily_android.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {
    fun getTime(dateTime: String): String {
        val localDateTime = LocalDateTime.parse(dateTime)
        val time = localDateTime.toLocalTime()
        if (time.hour >= 12) {
            return "오후 ${time.hour - 12}:${time.minute}"
        }
        return "오전 ${time.hour}:${time.minute}"
    }

    fun getFullDate(dateTime: String): String {
        val localDateTime = LocalDateTime.parse(dateTime)
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(localDateTime)
    }

    fun getMonthDay(dateTime: String): String {
        val localDateTime = LocalDateTime.parse(dateTime)
        return DateTimeFormatter.ofPattern("MM.dd").format(localDateTime)
    }

    fun getMonthDayPlusDays(dateTime: String, daysToAdd: Long): String {
        val localDateTime = LocalDateTime.parse(dateTime)
        val addedDateTime = localDateTime.plusDays(daysToAdd)
        return DateTimeFormatter.ofPattern("MM.dd").format(addedDateTime)
    }
}