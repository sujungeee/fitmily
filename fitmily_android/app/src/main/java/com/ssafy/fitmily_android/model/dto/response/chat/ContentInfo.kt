package com.ssafy.fitmily_android.model.dto.response.chat

data class ContentInfo(
    val messageType: String
    , val text: String? = ""
    , val imageUrl: String? = ""
)