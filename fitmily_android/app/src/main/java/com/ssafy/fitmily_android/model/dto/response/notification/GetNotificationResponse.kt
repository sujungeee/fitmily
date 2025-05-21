package com.ssafy.fitmily_android.model.dto.response.notification

import com.google.gson.annotations.SerializedName

data class GetNotificationResponse(
    @SerializedName("content") val notifications: List<Notification>
)