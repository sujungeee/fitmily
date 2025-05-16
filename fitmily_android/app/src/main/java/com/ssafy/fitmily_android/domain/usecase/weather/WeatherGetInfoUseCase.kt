package com.ssafy.fitmily_android.domain.usecase.weather

import com.ssafy.fitmily_android.domain.repository.WeatherRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse
import javax.inject.Inject

class WeatherGetInfoUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        exclude: String,
        weatherApiKey: String
    ): Result<WeatherResponse> {
        return weatherRepository.getWeatherInfo(
            lat = lat,
            lon = lon,
            exclude = exclude,
            weatherApiKey = weatherApiKey
        )
    }
}