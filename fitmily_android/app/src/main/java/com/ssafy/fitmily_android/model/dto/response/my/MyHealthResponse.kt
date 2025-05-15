package com.ssafy.fitmily_android.model.dto.response.my

data class MyHealthResponse(
    val bmi: Float,
    val createdAt: String,
    val fiveMajorDiseases: List<String>,
    val healthId: Int,
    val height: Float,
    val otherDiseases: List<String>,
    val updatedAt: String,
    val weight: Float
)