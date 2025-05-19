package com.ssafy.fitmily_android.model.dto.response.family

data class FamilyDailyMember(
    val exerciseGoalProgress: Int,
    val exercises: List<FamilyDailyExercise>,
    val totalCalories: Int,
    val totalTime: Int,
    val userFamilySequence: Int,
    val userId: Int,
    val userNickname: String
)