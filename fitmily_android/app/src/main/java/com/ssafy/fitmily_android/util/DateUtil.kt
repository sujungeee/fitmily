package com.ssafy.fitmily_android.util

import java.time.LocalDateTime

class DateUtil {
    fun getTime(timeStamp: String): String {
        val localDateTime = LocalDateTime.parse(timeStamp)
        val time = localDateTime.toLocalTime()
        if (time.hour >= 12) {
            return "오후 ${time.hour - 12}:${time.minute}"
        }
        return "오전 ${time.hour}:${time.minute}"
    }
}