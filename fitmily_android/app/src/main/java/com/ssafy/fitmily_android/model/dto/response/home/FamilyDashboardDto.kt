package com.ssafy.fitmily_android.model.dto.response.home

data class FamilyDashboardDto(
    val goals: List<GoalDto>,
    val totalProgressRate: Int,
    val userId: Int,
    val userNickname: String,
    val userZodiacName: String,
    val userFamilySequence: Int,
)