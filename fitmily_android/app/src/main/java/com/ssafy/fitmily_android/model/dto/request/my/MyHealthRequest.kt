package com.ssafy.fitmily_android.model.dto.request.my

data class MyHealthRequest(
    val fiveMajorDiseases: List<String>? = null,
    val height: Float? = null,
    val otherDiseases: List<String>? = null,
    val weight: Float? = null
)