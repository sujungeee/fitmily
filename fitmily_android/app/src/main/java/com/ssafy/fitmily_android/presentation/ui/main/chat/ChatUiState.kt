package com.ssafy.fitmily_android.presentation.ui.main.chat

import androidx.paging.PagingData
import com.ssafy.fitmily_android.model.dto.response.chat.ChatMessage
import com.ssafy.fitmily_android.model.dto.response.chat.WSChatResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ChatUiState(
    val chatPagingData: Flow<PagingData<ChatMessage>> = flowOf(PagingData.empty()) // 기존 채팅
    , val newMessages: List<WSChatResponse> = emptyList() // 웹소켓 채팅
    , val familyName: String = ""
    , val familyCount: Int = 0
)