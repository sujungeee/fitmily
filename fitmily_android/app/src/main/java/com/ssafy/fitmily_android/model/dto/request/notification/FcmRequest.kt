package com.ssafy.fitmily_android.model.dto.request.notification

import com.google.gson.annotations.SerializedName

data class FcmRequest(
    @SerializedName("userId") val userId: Int
    , @SerializedName("fcmToken") val fcmToken: String
)