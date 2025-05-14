package com.ssafy.fitmily_android.model.dto.response

import com.google.gson.annotations.SerializedName

data class ReissueResponse(
    @SerializedName("accessToken") val accessToken: String
    , @SerializedName("refreshToken") val refreshToken: String
)