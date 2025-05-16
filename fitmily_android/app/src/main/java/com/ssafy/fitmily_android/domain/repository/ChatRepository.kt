package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.chat.ChatListResponse

interface ChatRepository {
    suspend fun getChatList(familyId: String): Result<ChatListResponse>
}