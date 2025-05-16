package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.ChatRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.chat.ChatListResponse
import com.ssafy.fitmily_android.model.service.ChatService
import jakarta.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService
): ChatRepository {
    override suspend fun getChatList(familyId: String): Result<ChatListResponse> {
        return ApiResultHandler.handleApi {
            chatService.getChatList(familyId)
        }
    }
}