package com.ssafy.fitmily_android.model.dto.response.family

data class FamilyDailyExercise(
    val exerciseCalories: Int,
    val exerciseCount: Double,
    val exerciseGoalValue: Int,
    val exerciseId: Int?,
    val exerciseName: String,
    val exerciseRouteImg: String,
    val exerciseTime: Int
)