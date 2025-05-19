package com.ssafy.fitmily_android.presentation.ui.main.home.profile.component

import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyDashboardDto
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse

data class FamilyProfileUiState(
    val isLoading: Boolean = false,
    val tstMessage : String = "",
    val familyHealthListData: FamilyHealthResponse = FamilyHealthResponse(),
)
