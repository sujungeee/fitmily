package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.BuildConfig
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/3.0/onecall")
    suspend fun getWeatherInfo(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") weatherApiKey: String
    ): Response<WeatherResponse>
}