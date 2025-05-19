package com.ssafy.fitmily_android.model.dto.response.walk

data class HistoryDto(
    val userFamilySequence: Int,
    val userId: Int,
    val userNickname: String,
    val userZodiacName: String,
    val walkCalories: Int,
    val walkDistance: Double,
    val walkEndTime: String,
    val walkId: Int,
    val walkRouteImg: String,
    val walkStartTime: String
){
    constructor() : this(
        userFamilySequence = 0,
        userId = 0,
        userNickname = "",
        userZodiacName = "",
        walkCalories = 0,
        walkDistance = 0.0,
        walkEndTime = "",
        walkId = 0,
        walkRouteImg = "",
        walkStartTime = ""
    )
}