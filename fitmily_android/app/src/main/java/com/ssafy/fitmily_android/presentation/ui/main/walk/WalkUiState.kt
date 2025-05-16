package com.ssafy.fitmily_android.presentation.ui.main.walk

import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse

data class WalkUiState(
    val isLoading: Boolean = false,
    val tstMessage: String = "",
    val otherGpsList: List<GpsDto> = emptyList<GpsDto>(),
    val walkingFamilyList:List<WalkingFamilyResponse> = emptyList<WalkingFamilyResponse>()
)
