package com.ssafy.fitmily_android.model.dto.response.chat

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("messageId") val messageId: String
    , @SerializedName("senderId") val senderId: Int
    , @SerializedName("senderInfo") val senderInfo: SenderInfo
    , @SerializedName("content") val contentInfo: ContentInfo
    , @SerializedName("sendAt") val sendAt: String
    , @SerializedName("readByUserIds") val readByUserIds: List<Int>
    , @SerializedName("unReadCount") val unReadCount: Int
)