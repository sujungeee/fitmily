package com.ssafy.fitmily_android.model.dto.response.home

import com.google.gson.annotations.SerializedName

data class FamilyHealthResponse(
    @SerializedName("members") val familyHealthDto: List<FamilyHealthDto>
)