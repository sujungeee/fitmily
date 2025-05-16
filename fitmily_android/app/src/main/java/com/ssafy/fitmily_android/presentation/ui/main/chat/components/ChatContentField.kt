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
import com.ssafy.fitmily_android.presentation.ui.main.chat.ChatViewModel
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun ChatContentField (
    modifier : Modifier
    , chatViewModel: ChatViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val uiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()

    val chatItems = uiState.chatPagingData.collectAsLazyPagingItems()
    val serverChatSize = chatItems.itemCount
    val socketChatSize = uiState.newMessages.size
    val totalChatSize = serverChatSize + socketChatSize

    var userId by remember { mutableStateOf(-1) }
    LaunchedEffect(Unit) {
        userId = MainApplication.getInstance().getDataStore().getUserId()
    }

    if (totalChatSize > 0) {
        LaunchedEffect(totalChatSize) {
            scrollState.scrollToItem(totalChatSize - 1)
        }
    }

    // 채팅 리스트
    LazyColumn (
        modifier = modifier
        , state = scrollState
    ) {
        items(chatItems.itemCount) { index ->
            val chat = ChatMessage(
                profileColor = ProfileUtil().seqToColor(chatItems[index]!!.senderInfo.familySequence)!!
                , profileIcon = chatItems[index]!!.senderInfo.userZodiacName
                , nickname = chatItems[index]!!.senderInfo.userNickname
                , message = chatItems[index]!!.contentInfo.text
                , imageUrl = chatItems[index]!!.contentInfo.imageUrl
                , unReadCount = chatItems[index]!!.unReadCount
                , date = chatItems[index]!!.sendAt
            )
            if (chatItems[index]?.senderId == userId) {
                MyChatMessageItem(chat)
            } else {
                OthersChatMessageItem(chat)
            }
        }
    }
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