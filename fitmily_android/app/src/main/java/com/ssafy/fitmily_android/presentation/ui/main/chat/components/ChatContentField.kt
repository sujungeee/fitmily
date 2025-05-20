package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.model.dto.response.chat.ChatMessage
import com.ssafy.fitmily_android.presentation.ui.main.chat.ChatViewModel
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun ChatContentField (
    modifier : Modifier
    , chatViewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()

    var userId by remember { mutableStateOf(-1) }
    val scrollState = rememberLazyListState()
    var isScrolled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userId = MainApplication.getInstance().getDataStore().getUserId()
        // 기존 채팅 불러오기
        chatViewModel.getChatListFlow()
    }

    val chatItems = uiState.chatPagingData.collectAsLazyPagingItems()
    val serverChatSize = chatItems.itemCount
    val socketChatSize = uiState.newMessages.size
    val totalChatSize = serverChatSize + socketChatSize

    // 처음 진입 시, 맨 아래로 스크롤
    LaunchedEffect(serverChatSize) {
        if (serverChatSize > 0 && !isScrolled) {
            scrollState.scrollToItem(serverChatSize - 1)
            isScrolled = true
        }
    }

    // 새 채팅이 왔을 시, 맨 아래에 있으면 다시 맨 아래로 스크롤
    LaunchedEffect (socketChatSize){
        val isBottom = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == totalChatSize - 1
        if (socketChatSize > 0 && isBottom) {
            scrollState.scrollToItem(totalChatSize - 1)
        }
    }

    // 채팅 리스트
    LazyColumn (
        modifier = modifier
        , state = scrollState
    ) {
        // 기존 채팅
        items(chatItems.itemCount) { index ->
            val chat = toChatItem(chatItems[index]!!)
            if (chatItems[index]?.senderId == userId) {
                MyChatMessageItem(chat)
            } else {
                OthersChatMessageItem(chat)
            }
        }

        // 웹소켓 채팅
        items(uiState.newMessages.size) { index ->
            val newChat = uiState.newMessages[index].message
            val chat = toChatItem(newChat)
            if (newChat.senderId == userId) {
                MyChatMessageItem(chat)
            } else {
                OthersChatMessageItem(chat)
            }
        }
    }
}

fun toChatItem(chatMessage: ChatMessage): com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatMessage {
    return ChatMessage(
        profileColor = ProfileUtil().seqToColor(chatMessage.senderInfo.familySequence)!!
        , profileIcon = chatMessage.senderInfo.userZodiacName
        , nickname = chatMessage.senderInfo.userNickname
        , message = chatMessage.contentInfo.text
        , imageUrl = chatMessage.contentInfo.imageUrl
        , unReadCount = chatMessage.unreadCount
        , date = chatMessage.sentAt
    )
}

data class ChatMessage(
    val profileColor: Color
    , val profileIcon: String
    , val nickname: String
    , val message: String?
    , val imageUrl: String?
    , val unReadCount: Int
    , val date: String
)