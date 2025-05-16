package com.ssafy.fitmily_android.presentation.ui.main.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.domain.usecase.chat.ChatListUseCase
import com.ssafy.fitmily_android.model.dto.request.chat.ChatSendRequest
import com.ssafy.fitmily_android.network.ChatManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.convertAndSend
import org.hildan.krossbow.stomp.conversions.moshi.withMoshi
import org.hildan.krossbow.stomp.frame.StompFrame
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.chat.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

private const val TAG = "ChatViewModel_fitmily"
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatListUseCase: ChatListUseCase
) : ViewModel () {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    private lateinit var familyId: String

    private val chatClient = ChatManager()
    private lateinit var stompSession: StompSession
    private lateinit var chatTopic: Flow<StompFrame.Message>
    val authDataStore = MainApplication.getInstance().getDataStore()

    var chatList: Flow<PagingData<ChatMessage>> ?= null

    fun initStomp() {
        viewModelScope.launch {
            runCatching {
//                familyId = authDataStore.getFamilyId()
                familyId = 1.toString() // TODO: delete

                stompSession = chatClient.stompClient.connect(
                    url = chatClient.url
                    , customStompConnectHeaders = mapOf("Authorization" to "tmpAccessToken") // TODO: edit to authDataStore.getAccessToken()
                ).withMoshi(chatClient.moshi)

                // 채팅방 구독
                chatTopic = stompSession.subscribe(
                    StompSubscribeHeaders (
                        destination = "$SUBSCRIBE_URL$familyId"
                        , customHeaders = mapOf("Authorization" to "tmpAccessToken")
                    )
                )

                // 채팅 수신
                chatTopic.collect {
                    val newMessage = chatClient.moshi.adapter(ChatMessage::class.java).fromJson(it.bodyAsText)
                    _chatUiState.update { state ->
                        state.copy(newMessages = state.newMessages + newMessage!!)
                    }
                }
            }
        }
    }

    fun cancelStomp() {
        viewModelScope.launch {
            stompSession.disconnect()
        }
    }

    // 채팅 전송
    fun sendMessage(type: String, message: String? = "", images: List<Uri>? = listOf()) {
        viewModelScope.launch {
            runCatching {
                if (type.equals("image")) { // 이미지 전송(하나씩)
                    for (image in images!!) {
                        // TODO: presigned url -> S3 upload


                        stompSession.withMoshi(chatClient.moshi).convertAndSend(
                            StompSendHeaders(
                                destination = "$SEND_URL$familyId",
                                customHeaders = mapOf("Authorization" to "tmpAccessToken")
                            )
                            , ChatSendRequest(type, "", image.toString())
                        )
                    }
                } else { // 메시지 전송
                    stompSession.withMoshi(chatClient.moshi).convertAndSend(
                        StompSendHeaders(
                            destination = "$SEND_URL$familyId",
                            customHeaders = mapOf("Authorization" to "tmpAccessToken")
                        )
                        , ChatSendRequest(type, message, "")
                    )
                }
            }
        }
    }

    fun getChatListFlow() {
        chatList = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ChatPagingSource(familyId, chatListUseCase)
            }
        ).flow
            .cachedIn(viewModelScope)

        _chatUiState.update {
            it.copy(chatPagingData = chatList!!)
        }
    }

    companion object {
        const val SEND_URL = "/app/chat.sendMessage/family/"
        const val SUBSCRIBE_URL = "/topic/chat/family/"
    }
}