package com.ssafy.fitmily_android.presentation.ui.main.chat

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ssafy.fitmily_android.domain.usecase.chat.ChatListUseCase
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.chat.ChatMessage

class ChatPagingSource(
    private val familyId: String,
    private val chatListUseCase: ChatListUseCase
) : PagingSource<Int, ChatMessage>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatMessage> {
        val page = params.key ?: 1

        return try {
            val result = chatListUseCase(familyId, page)
            if (result is Result.Success) {
                var messages = emptyList<ChatMessage>()
                result.data?.let {
                    messages = it.messages
                }
                val isEnd = messages.size < PAGE_SIZE
                LoadResult.Page(
                    data = messages,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (isEnd) null else page + 1
                )
            } else {
                LoadResult.Error(Throwable(result.toString()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ChatMessage>): Int? {
        return null
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}