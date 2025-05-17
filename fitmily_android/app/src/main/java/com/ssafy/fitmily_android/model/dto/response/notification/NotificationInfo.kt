package com.ssafy.fitmily_android.model.dto.response.notification

import com.google.gson.annotations.SerializedName

data class NotificationInfo(
    @SerializedName("pokeId") val pokeId: Int = -1,
    @SerializedName("challengeId") val challengeId: Int = -1,
    @SerializedName("walkId") val walkId: Int = -1,
    @SerializedName("startDate") val startDate: String = ""
)
