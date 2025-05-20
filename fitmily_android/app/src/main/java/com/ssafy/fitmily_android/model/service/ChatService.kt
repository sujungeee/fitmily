package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.response.chat.ChatListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService {
    @GET("chat/family/{familyId}/{page}/messages")
    suspend fun getChatList(
        @Path("familyId") familyId: String
        , @Path("page") page: Int
    ): Response<ChatListResponse>
}