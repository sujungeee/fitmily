package com.ssafy.fitmily_android.model.dto.response.notification

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("notificationId") val notificationId: Int
    , @SerializedName("type") val type: String
    , @SerializedName("receivedAt") val receivedAt: String
    , @SerializedName("senderId") val senderId: Int
    , @SerializedName("senderNickname") val senderNickname: String
    , @SerializedName("data") val notificationInfo: NotificationInfo
)