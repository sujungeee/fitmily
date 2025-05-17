package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.auth.JoinRequest
import com.ssafy.fitmily_android.model.dto.request.auth.LoginRequest
import com.ssafy.fitmily_android.model.dto.request.auth.ReissueRequest
import com.ssafy.fitmily_android.model.dto.response.auth.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.auth.ReissueResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/reissue")
    suspend fun reissue(
        @Body reissueRequest: ReissueRequest
    ): Response<ReissueResponse>

    @GET("users/check-id")
    suspend fun checkDuplId(
        @Query("username") userId: String
    ): Response<Boolean>

    @POST("users")
    suspend fun join(
        @Body joinRequest: JoinRequest
    ): Response<Unit>
}