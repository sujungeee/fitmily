package com.ssafy.fitmily_android.model.dto.response.home

data class ChallengeMemberDto(
    val distanceCompleted: Double,
    val nickname: String,
    val familySequence: Int,
    val zodiacName: String,
    val rank: Int,
    val userId: Int
){
    constructor() : this(
        distanceCompleted = 0.0,
        nickname = "--",
        familySequence = 0,
        zodiacName = "",
        rank = 0,
        userId = 0
    )
}