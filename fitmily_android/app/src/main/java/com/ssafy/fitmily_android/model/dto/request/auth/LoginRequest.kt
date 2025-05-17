package com.ssafy.fitmily_android.model.dto.request.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("loginId") val userId: String
    , @SerializedName("password") val userPwd: String
)