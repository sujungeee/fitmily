package com.ssafy.fitmily_android.model.dto.response.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("userId") val userId: Int
    , @SerializedName("userNickname") val userNickname: String
    , @SerializedName("familyId") val familyId: Int
    , @SerializedName("zodiacName") val zodiacName: String
    , @SerializedName("accessToken") val accessToken: String
    , @SerializedName("refreshToken") val refreshToken: String
)