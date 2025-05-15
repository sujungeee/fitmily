package com.ssafy.fitmily_android.model.dto.request.walk

data class WalkEndRequest(
    val walkDistance: Double,
    val walkEndTime: String,
    val walkRouteImg: String,
    val walkStartTime: String
)