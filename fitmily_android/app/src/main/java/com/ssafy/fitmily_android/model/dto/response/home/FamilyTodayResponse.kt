package com.ssafy.fitmily_android.model.dto.response.home

import com.google.gson.annotations.SerializedName

data class FamilyTodayResponse(
    val date: String,
    @SerializedName("members") val familyDashboardDto: List<FamilyDashboardDto>
)