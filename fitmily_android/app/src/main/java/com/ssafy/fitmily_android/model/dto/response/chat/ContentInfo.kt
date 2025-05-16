package com.ssafy.fitmily_android.model.dto.response.chat

import com.google.gson.annotations.SerializedName

data class ContentInfo(
    @SerializedName("messageType") val messageType: String
    , @SerializedName("text") val text: String?
    , @SerializedName("imageUrl") val imageUrl: String?
)