package com.ssafy.fitmily_android.model.dto.response.walk

data class WalkingFamilyResponse(
    val userFamilySequence: Int,
    val userId: Int,
    val userNickname: String,
    val userZodiacName: String
)