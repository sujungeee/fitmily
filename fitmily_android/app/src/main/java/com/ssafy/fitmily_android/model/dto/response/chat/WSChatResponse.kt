package com.ssafy.fitmily_android.model.dto.response.chat

import com.google.gson.annotations.SerializedName

data class WSChatResponse(
    @SerializedName("type") val type: String
    , @SerializedName("data") val message: ChatMessage
)