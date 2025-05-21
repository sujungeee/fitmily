package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.model.dto.response.chat.ChatMessage
import com.ssafy.fitmily_android.presentation.ui.main.chat.ChatViewModel
import com.ssafy.fitmily_android.util.ProfileUtil

private const val TAG = "ChatContentField_fitmily"
@Composable
fun ChatContentField (
    modifier : Modifier
    , scrollState: LazyListState
    , onSizeChanged: (Int) -> Unit
    , chatViewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()
    var isHeightInitialized by remember { mutableStateOf(false) }
    var initialLazyColumnHeight by remember { mutableStateOf(-1) }
    var lazyColumnHeight by remember { mutableStateOf(0) }
    var userId by remember { mutableStateOf(-1) }

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
        if (serverChatSize > 0) {
            scrollState.scrollToItem(serverChatSize - 1)
        }
    }

    // 새 채팅이 왔을 시, 맨 아래 범위 안(-3)에 있으면 다시 맨 아래로 스크롤
    LaunchedEffect (socketChatSize) {
        Log.d(TAG, "ChatContentField: socket size: ${socketChatSize}")
        val lastVisibleIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        val isNearBottom = lastVisibleIndex >= totalChatSize - 3
        if (socketChatSize > 0 && isNearBottom) {
            scrollState.scrollToItem(totalChatSize - 1)
        }
    }

    // 채팅 전체 size에 따라, 갤러리 화면이 올라올 시 맨 아래로 스크롤
    LaunchedEffect (totalChatSize) {
        onSizeChanged(totalChatSize)
        Log.d(TAG, "ChatContentField: total size: ${totalChatSize}")
    }

    // 키보드 올라올 시, 맨 아래로 스크롤
    LaunchedEffect (lazyColumnHeight) {
        if (initialLazyColumnHeight != -1 && lazyColumnHeight < initialLazyColumnHeight) {
            scrollState.scrollToItem(totalChatSize - 1)
        }
    }

    // 채팅 리스트
    LazyColumn (
        modifier = modifier.onGloballyPositioned {
            if (!isHeightInitialized) {
                initialLazyColumnHeight = it.size.height
                isHeightInitialized = true
            }
            if (lazyColumnHeight != it.size.height) {
                lazyColumnHeight = it.size.height
            }
        }
        , state = scrollState
    ) {
        // 기존 채팅
        items(chatItems.itemCount) { index ->
            val chat = toChatItem(chatItems[index]!!)
            if (chatItems[index]?.senderId == userId.toString()) {
                MyChatMessageItem(chat)
            } else {
                OthersChatMessageItem(chat)
            }
        }

        // 웹소켓 채팅
        items(uiState.newMessages.size) { index ->
            val newChat = uiState.newMessages[index].data
            val chat = toChatItem(newChat)
            if (newChat.senderId == userId.toString()) {
                MyChatMessageItem(chat)
            } else {
                OthersChatMessageItem(chat)
            }
        }
    }
}

fun toChatItem(chatMessage: ChatMessage): com.ssafy.fitmily_android.presentation.ui.main.chat.components.ChatMessage {
    return ChatMessage(
        profileColor = ProfileUtil().seqToColor(chatMessage.senderInfo.familySequence.toInt())!!
        , profileIcon = chatMessage.senderInfo.userZodiacName
        , nickname = chatMessage.senderInfo.nickname
        , message = chatMessage.content.text
        , imageUrl = chatMessage.content.imageUrl
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