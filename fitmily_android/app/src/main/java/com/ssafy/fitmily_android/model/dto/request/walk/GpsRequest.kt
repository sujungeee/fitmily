package com.ssafy.fitmily_android.model.dto.request.walk

import com.google.firebase.Timestamp

data class GpsRequest(
    val userId: Int,
        val lat : Double,
        val lon : Double,
        val timestamp: String)