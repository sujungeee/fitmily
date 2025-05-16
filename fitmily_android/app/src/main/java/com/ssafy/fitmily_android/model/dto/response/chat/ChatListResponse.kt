package com.ssafy.fitmily_android.model.dto.response.chat

import com.google.gson.annotations.SerializedName

data class ChatListResponse(
    @SerializedName("messages") val messages: List<ChatMessage>
    , @SerializedName("hasMore") val hasMore: Boolean
)