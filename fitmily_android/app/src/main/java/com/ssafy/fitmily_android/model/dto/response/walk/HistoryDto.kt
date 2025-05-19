package com.ssafy.fitmily_android.model.dto.response.walk

data class HistoryDto(
    val userFamilySequence: Int,
    val userId: Int,
    val nickname: String,
    val zodiacName: String,
    val calories: Int,
    val distance: Double,
    val endTime: String,
    val walkId: Int,
    val routeImg: String,
    val startTime: String
){
    constructor() : this(
        userFamilySequence = 0,
        userId = 0,
        nickname = "",
        zodiacName = "",
        calories = 0,
        distance = 0.0,
        endTime = "",
        walkId = 0,
        routeImg = "",
        startTime = ""
    )
}