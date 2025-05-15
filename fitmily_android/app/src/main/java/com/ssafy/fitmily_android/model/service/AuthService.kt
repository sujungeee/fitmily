package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.FcmRequest
import com.ssafy.fitmily_android.model.dto.request.JoinRequest
import com.ssafy.fitmily_android.model.dto.request.LoginRequest
import com.ssafy.fitmily_android.model.dto.request.ReissueRequest
import com.ssafy.fitmily_android.model.dto.response.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.ReissueResponse
import com.ssafy.fitmily_android.model.dto.response.FcmResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("auth/logout")
    suspend fun logout(): Boolean

    @POST("auth/reissue")
    suspend fun reissue(
        @Body reissueRequest: ReissueRequest
    ): ReissueResponse

    @GET("users/check-id")
    suspend fun checkDuplId(
        @Query("username") userId: String
    ): Boolean

    @POST("users")
    suspend fun join(
        @Body joinRequest: JoinRequest
    ): Boolean

    @POST("fcm/token")
    suspend fun sendFcmToken(
        @Body fcmRequest: FcmRequest
    ): FcmResponse
}