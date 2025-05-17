package com.ssafy.fitmily_android.model.dto.response.notification

import com.google.gson.annotations.SerializedName

data class UnReadNotificationResponse (
    @SerializedName("hasUnreadNotification") val hasUnreadNotification: Boolean
    , @SerializedName("unreadCount") val unreadCount: Int
)