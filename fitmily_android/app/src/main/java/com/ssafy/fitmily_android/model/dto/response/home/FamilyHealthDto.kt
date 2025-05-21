package com.ssafy.fitmily_android.model.dto.response.home

data class FamilyHealthDto(
    val healthBmi: Int,
    val healthFiveMajorDiseases: List<String>,
    val healthHeight: Double,
    val healthOtherDiseases: List<String>,
    val healthWeight: Double,
    val userBirth: String,
    val userGender: Int,
    val userId: Int,
    val userNickname: String,
    val userFamilySequence: Int,
    val userZodiacName: String
)