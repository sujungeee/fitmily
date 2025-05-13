package com.ssafy.fitmily_android.model.dto.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("user_login_id") val userLoginId: String
    , @SerializedName("user_pw") val userPw: String
)