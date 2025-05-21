package com.ssafy.fitmily_android.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateUtil {
    fun getTime(dateTime: String): String {
        val odt = OffsetDateTime.parse(dateTime)
        val zonedDateTime = odt.atZoneSameInstant(ZoneId.systemDefault())

        val hour = zonedDateTime.hour
        val minute = zonedDateTime.minute.toString().padStart(2, '0')

        if (hour >= 12) {
            return "오후 ${hour - 12}:${minute}"
        }
        return "오전 ${hour}:${minute}"
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

    fun getDday(date: String): String {
        val targetDate = LocalDate.parse(date)
        val currentDate = LocalDate.now()
        val daysUntil = java.time.temporal.ChronoUnit.DAYS.between(currentDate, targetDate)
        return when {
            daysUntil > 0 -> "D+$daysUntil"
            daysUntil == 0L -> "D-day"
            else -> "D-${-daysUntil}"
        }
    }

    fun getChallengeDate(date: String): String {
        val localDate = LocalDate.parse(date)
        val addedDate = localDate.plusDays(7)
        return localDate.format(DateTimeFormatter.ofPattern("MM.dd")) + " ~ " + addedDate.format(DateTimeFormatter.ofPattern("MM.dd"))
    }


    fun getDurationTime(startTime: String, endTime: String): String {
        val start = LocalDateTime.parse(startTime)
        val end = LocalDateTime.parse(endTime)
        val duration = java.time.Duration.between(start, end)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}