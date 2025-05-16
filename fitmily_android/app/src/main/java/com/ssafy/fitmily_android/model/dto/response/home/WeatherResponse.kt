package com.ssafy.fitmily_android.model.dto.response.home

data class WeatherResponse(
    val alerts: List<Any>,
    val current: WeatherCurrentDto,
    val daily: List<Any>,
    val hourly: List<Any>,
    val lat: Double,
    val lon: Double,
    val minutely: List<Any>,
    val timezone: String,
    val timezone_offset: Int
)