package com.ssafy.fitmily_android.presentation.ui.main.walk.history

import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import com.ssafy.fitmily_android.model.dto.response.walk.WalkHistoryResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse

data class WalkHistoryUiState(
    val isLoading: Boolean = false,
    val tstMessage: String = "",
    val walkHistoryResponse: WalkHistoryResponse = WalkHistoryResponse(
        walk = emptyList()
    ),
)
