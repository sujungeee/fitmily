package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.home.FamilyCreateRequest
import com.ssafy.fitmily_android.model.dto.request.home.FamilyJoinRequest
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyJoinResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {
    @POST("family")
    suspend fun createFamily(
        @Body familyCreateRequest: FamilyCreateRequest
    ): Response<FamilyJoinResponse>

    @POST("family/join")
    suspend fun joinFamily(
        @Body familyJoinRequest: FamilyJoinRequest
    ): Response<FamilyJoinResponse>

    @GET("family/{familyId}")
    suspend fun getFamily(
        @Path("familyId") familyId: Int
    ): Response<FamilyResponse>

    @GET("family/{familyId}/dashboard")
    suspend fun getDashboard(
        @Path("familyId") familyId: Int,
    ): Response<FamilyTodayResponse>

    @GET("family/{familyId}/health-status")
    suspend fun getFamilyHealth(
        @Path("familyId") familyId: Int,
    ): Response<FamilyHealthResponse>

    @GET("challenge")
    suspend fun getChallenge(
    ): Response<ChallengeResponse>

    @POST("poke/{userId}")
    suspend fun sendPoke(
        @Path("userId") userId: Int
    ): Response<Unit>

}