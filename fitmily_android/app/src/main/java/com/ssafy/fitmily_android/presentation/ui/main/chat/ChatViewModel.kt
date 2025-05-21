package com.ssafy.fitmily_android.presentation.ui.main.chat

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.domain.usecase.chat.ChatListUseCase
import com.ssafy.fitmily_android.domain.usecase.home.GetFamilyUseCase
import com.ssafy.fitmily_android.domain.usecase.s3.GetPresignedUrlUseCase
import com.ssafy.fitmily_android.domain.usecase.s3.S3UseCase
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
import com.ssafy.fitmily_android.model.dto.response.chat.ChatMessage
import com.ssafy.fitmily_android.model.dto.response.chat.WSChatResponse
import com.ssafy.fitmily_android.util.FileUtil
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody

private const val TAG = "ChatViewModel_fitmily"
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getFamilyUseCase: GetFamilyUseCase
    , private val chatListUseCase: ChatListUseCase
    , private val getPresignedUrlUseCase: GetPresignedUrlUseCase
    , private val s3UseCase: S3UseCase
) : ViewModel () {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    private lateinit var familyId: String

    private val chatClient = ChatManager()
    private lateinit var stompSession: StompSession
    private lateinit var chatTopic: Flow<StompFrame.Message>
    var chatList: Flow<PagingData<ChatMessage>> ?= null

    val authDataStore = MainApplication.getInstance().getDataStore()

    fun initStomp() {
        viewModelScope.launch {
            try {
                familyId = authDataStore.getFamilyId().toString()

                // 스톰프 연결
                stompSession = chatClient.stompClient.connect(
                    url = chatClient.url
                    , customStompConnectHeaders = mapOf(
                        "Authorization" to "Bearer ${authDataStore.getAccessToken()}"
                    )
                ).withMoshi(chatClient.moshi)
                Log.d(TAG, "initStomp: 스톰프 연결: ${stompSession}")

                // 채팅방 구독
                chatTopic = stompSession.subscribe(
                    StompSubscribeHeaders (
                        destination = "$SUBSCRIBE_URL$familyId"
                        , customHeaders = mapOf(
                            "Authorization" to "Bearer ${authDataStore.getAccessToken()}"
                        )
                    )
                )
                Log.d(TAG, "initStomp: 구독: ${chatTopic}")

                // 채팅 수신
                chatTopic.collect {
                    val newMessage = chatClient.moshi.adapter(WSChatResponse::class.java).fromJson(it.bodyAsText)
                    Log.d(TAG, "initStomp: 수신: $newMessage")
                    _chatUiState.update { state ->
                        state.copy(newMessages = state.newMessages + newMessage!!)
                    }
                }
            } catch (e: Exception) {
                initStomp()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::stompSession.isInitialized) {
            try {
                runBlocking { stompSession.disconnect() }
                Log.d(TAG, "cancelStomp: ")
            } catch (e: Exception) {
                Log.e(TAG, "cancelStomp: ${e.message}")
            }
        }
    }

    // 채팅 전송
    fun sendMessage(context: Context, type: String, message: String? = "", images: List<Uri>? = listOf()) {
        viewModelScope.launch {
            runCatching {
                if (type.equals("image")) {
                    // 이미지 전송(하나씩)
                    for (image in images!!) {
                        val s3Image = imageUpload(context, image)
                        Log.d(TAG, "sendMessage: s3Image: ${s3Image}")
                        stompSession.withMoshi(chatClient.moshi).convertAndSend(
                            StompSendHeaders(
                                destination = "$SEND_URL$familyId",
                                customHeaders = mapOf(
                                    "Authorization" to "Bearer ${authDataStore.getAccessToken()}"
                                )
                            )
                            , ChatSendRequest(type, "", s3Image)
                        )
                        Log.d(TAG, "sendMessage: 이미지 전송")
                    }
                } else {
                    // 메시지 전송
                    stompSession.withMoshi(chatClient.moshi).convertAndSend(
                        StompSendHeaders(
                            destination = "$SEND_URL$familyId",
                            customHeaders = mapOf(
                                "Authorization" to "Bearer ${authDataStore.getAccessToken()}"
                            )
                        )
                        , ChatSendRequest(type, message, "")
                    )
                    Log.d(TAG, "sendMessage: 텍스트 전송")
                }
            }
        }
    }

    // 이미지 업로드
    suspend fun imageUpload(context: Context, image: Uri): String {
        runCatching {
            Log.d(TAG, "imageUpload: 함수 발동")
            val fileName = FileUtil().getFileName(context, image)
            val fileExtension = FileUtil().getFileExtension(context, image)
            val contentType = when(fileExtension) {
                "jpg" -> "image/jpeg"
                "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                else -> null
            }
            val getPresignedUrlResult = getPresignedUrlUseCase(fileName!!, contentType!!)
            ViewModelResultHandler.handle(
                result = getPresignedUrlResult
                , onSuccess = { data ->
                    Log.d(TAG, "imageUpload: presignedUrl: ${data}")
                    // upload
                    val file = FileUtil().getFileFromUri(context, image, fileName, fileExtension!!)
                    val requestBody = file.asRequestBody(contentType.toMediaType())
                    val uploadResult = s3UseCase(data!!.url, requestBody)
                    ViewModelResultHandler.handle(
                        result = uploadResult
                        , onSuccess = {
                            Log.d(TAG, "imageUpload: 성공")
                            return fileName
                        }
                        , onError = { msg ->
                            Log.d(TAG, "imageUpload: presignedUrl: ${msg}")
                        }
                    )
                }
                , onError = { msg ->
                    Log.d(TAG, "getPresignedUrl: ${msg}")
                }
            )
        }
        return ""
    }

    // 기존 채팅 불러오기
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

    // 가족 정보 불러오기
    fun getFamilyInfo(familyId: Int) {
        viewModelScope.launch {
            val result = getFamilyUseCase(familyId)
            ViewModelResultHandler.handle(
                result = result
                , onSuccess = { data ->
                    _chatUiState.update {
                        it.copy(
                            familyName = data!!.familyName
                            , familyCount = data.familyPeople
                        )
                    }
                }
                , onError = { msg ->
                    Log.d(TAG, "getFamilyInfo: ${msg}")
                }
            )
        }
    }

    companion object {
        const val SEND_URL = "/app/chat.sendMessage/family/"
        const val SUBSCRIBE_URL = "/topic/chat/family/"
    }
}