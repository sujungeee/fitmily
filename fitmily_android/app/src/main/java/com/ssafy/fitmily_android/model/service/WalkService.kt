package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.WalkEndResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkHistoryResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkPathResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WalkService {
    @POST("walks/end")
    suspend fun postWalk(
        @Body walkEndRequest: WalkEndRequest
    ): Response<WalkEndResponse>

    @GET("walks")
    suspend fun getWalkHistory(
    ): Response<WalkHistoryResponse>

    @GET("gps/{userId}")
    suspend fun getWalkPath(
        @Path("userId") userId: Int
    ): Response<WalkPathResponse>

    @GET("walks/goal/exist")
    suspend fun getWalkGoalExist(
    ): Response<Boolean>

    @GET("family/walking-members")
    suspend fun getWalkingMembers(
        @Query("familyId") familyId: Int
    ): Response<WalkingFamilyResponse>

}