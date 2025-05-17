package com.ssafy.fitmily_android.model.dto.request.auth

import com.google.gson.annotations.SerializedName

data class JoinRequest(
    @SerializedName("loginId") val userLoginId: String
    , @SerializedName("password") val userPw: String
    , @SerializedName("nickname") val userNickname: String
    , @SerializedName("birth") val userBirth: String
    , @SerializedName("gender") val userGender: Int
)