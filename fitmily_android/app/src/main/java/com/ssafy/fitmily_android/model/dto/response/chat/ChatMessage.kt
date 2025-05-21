package com.ssafy.fitmily_android.model.dto.response.chat

data class ChatMessage(
    val messageId: String
    , val familyId: String
    , val senderId: String
    , val senderInfo: SenderInfo
    , val content: ContentInfo
    , val sentAt: String
    , val readByUserIds: List<String>
    , val unreadCount: Int
)