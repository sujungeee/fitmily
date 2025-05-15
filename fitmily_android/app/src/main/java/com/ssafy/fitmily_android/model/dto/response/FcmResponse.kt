package com.ssafy.fitmily_android.model.dto.response

import com.google.gson.annotations.SerializedName

data class FcmResponse(
    @SerializedName("fcmId") val fcmId: String
    , @SerializedName("userId") val userId: String
    , @SerializedName("registedAt") val registedAt: String
)
