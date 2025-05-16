package com.ssafy.fitmily_android.presentation.ui.main.home

import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyDashboardDto
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse

data class HomeUiState(
    val isLoading: Boolean = false,
    val tstMessage : String = "",
    val weather: WeatherResponse? = null,
    val familyId : Int = 0,
    val family :FamilyResponse  = FamilyResponse(),
    val dashBoardListData : FamilyTodayResponse = FamilyTodayResponse(),
    val challengeData: ChallengeResponse = ChallengeResponse(),
    val familyHealthListData: FamilyHealthResponse = FamilyHealthResponse(),
)
