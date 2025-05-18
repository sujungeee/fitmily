package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.my.MyHealthRequest
import com.ssafy.fitmily_android.model.dto.response.my.MyHealthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface MyHealthService {

    @GET("health")
    suspend fun getMyHealthInfo(): Response<MyHealthResponse>

    @POST("health")
    suspend fun insertMyHealthInfo(
        @Body myHealthRequest: MyHealthRequest
    ): Response<Unit>

    @PATCH("health")
    suspend fun updateMyHealthInfo(
        @Body myHealthRequest: MyHealthRequest
    ): Response<Unit>
}