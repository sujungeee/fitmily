package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.WeatherRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse
import com.ssafy.fitmily_android.model.service.WeatherService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
  private val weatherService: WeatherService
): WeatherRepository {

    override suspend fun getWeatherInfo(
        lat: Double,
        lon: Double,
        exclude: String,
        weatherApiKey: String
    ): Result<WeatherResponse> {
        return ApiResultHandler.handleApi {
            weatherService.getWeatherInfo(
                lat = lat,
                lon = lon,
                exclude = exclude,
                weatherApiKey = weatherApiKey
            )
        }
    }

}