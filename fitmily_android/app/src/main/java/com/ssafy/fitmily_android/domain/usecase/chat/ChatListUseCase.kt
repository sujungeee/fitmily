package com.ssafy.fitmily_android.domain.usecase.chat

import com.ssafy.fitmily_android.domain.repository.ChatRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.chat.ChatListResponse
import jakarta.inject.Inject

class ChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
){
    suspend operator fun invoke(familyId: String): Result<ChatListResponse> {
        return chatRepository.getChatList(familyId)
    }
}