package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.dto.response.my.MyHealthResponse
import retrofit2.Response

interface MyHealthRepository {

    suspend fun getMyHealthInfo(): Response<MyHealthResponse>

    suspend fun insertMyHealthInfo(
        fiveMajorDiseases: List<String>,
        height: Float,
        otherDiseases: List<String>,
        weight: Float
    ): Response<Any>

    suspend fun updateMyHealthInfo(
        fiveMajorDiseases: List<String>? = null,
        height: Float? = null,
        otherDiseases: List<String>? = null,
        weight: Float? = null
    ): Response<Any>
}