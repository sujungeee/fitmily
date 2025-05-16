package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse

interface WeatherRepository {
    suspend fun getWeatherInfo(
        lat: Double,
        lon: Double,
        exclude: String,
        weatherApiKey: String
    ): Result<WeatherResponse>
}