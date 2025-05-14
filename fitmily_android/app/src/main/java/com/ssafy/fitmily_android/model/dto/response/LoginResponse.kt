package com.ssafy.fitmily_android.model.dto.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user_id") val userId: String
    , @SerializedName("user_nickname") val userNickname: String
    , @SerializedName("user_profile_img") val userProfileImg: String
    , @SerializedName("user_color") val userColor: String
    , @SerializedName("access_token") val accessToken: String
    , @SerializedName("refresh_token") val refreshToken: String
)