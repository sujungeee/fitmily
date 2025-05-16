package com.ssafy.fitmily_android.presentation.ui.state

import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val weather: WeatherResponse? = null
)