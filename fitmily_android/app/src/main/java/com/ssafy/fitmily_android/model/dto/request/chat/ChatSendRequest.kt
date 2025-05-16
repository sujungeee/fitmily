package com.ssafy.fitmily_android.model.dto.request.chat

data class ChatSendRequest (
    val messageType: String
    , val content: String ?= null
    , val imageUrl: String ?= null
)