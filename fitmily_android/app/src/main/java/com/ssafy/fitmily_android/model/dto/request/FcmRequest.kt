package com.ssafy.fitmily_android.model.dto.request

import com.google.gson.annotations.SerializedName

data class FcmRequest(
    @SerializedName("userId") val userId: String
    , @SerializedName("fcmToken") val fcmToken: String
)