package com.ssafy.fitmily_android.model.dto.request

import com.google.gson.annotations.SerializedName

data class JoinRequest(
    @SerializedName("user_login_id") val userLoginId: String
    , @SerializedName("user_pw") val userPw: String
    , @SerializedName("user_nickname") val userNickname: String
    , @SerializedName("user_birth") val userBirth: String
    , @SerializedName("user_gender") val userGender: Int
)