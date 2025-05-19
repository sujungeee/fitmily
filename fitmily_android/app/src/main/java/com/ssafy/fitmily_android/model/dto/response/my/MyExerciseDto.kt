package com.ssafy.fitmily_android.model.dto.response.my

data class MyExerciseDto(
    val exerciseCalories: Int,
    val exerciseName: String,
    val exerciseRecord: Float,
    val imgUrl: String?,
    val walkId: Int?
)