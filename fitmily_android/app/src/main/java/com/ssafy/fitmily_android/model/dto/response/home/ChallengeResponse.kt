package com.ssafy.fitmily_android.model.dto.response.home

data class ChallengeResponse(
    val challengeId: Int,
    val participants: List<ChallengeMemberDto>,
    val progressPercentage: Int,
    val startDate: String,
    val targetDistance: Int
){
    constructor() : this(
        challengeId = 0,
        participants = emptyList(),
        progressPercentage = 0,
        startDate = "",
        targetDistance = 0
    )
}