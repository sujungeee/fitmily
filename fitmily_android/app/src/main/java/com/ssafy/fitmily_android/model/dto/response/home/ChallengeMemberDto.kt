package com.ssafy.fitmily_android.model.dto.response.home

data class ChallengeMemberDto(
    val distanceCompleted: Double,
    val nickname: String,
    val profileColor: String,
    val rank: Int,
    val userId: Int
)