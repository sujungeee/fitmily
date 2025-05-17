package com.ssafy.fitmily_android.model.dto.request.auth

import com.google.gson.annotations.SerializedName

data class ReissueRequest(
    @SerializedName("refreshToken") val refreshToken: String
)