package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.my.MyGoalRequest
import com.ssafy.fitmily_android.model.dto.response.my.MyGoalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MyGoalService {

    @GET("goals")
    suspend fun getMyGoalInfo(): Response<MyGoalResponse>

    @POST("goals")
    suspend fun insertMyGoalInfo(
        @Body myGoalRequest: MyGoalRequest
    ): Response<Unit>

    @PATCH("goals/{goalId}")
    suspend fun patchMyGoalInfo(
        @Path("goalId") goalId: Int, @Body myGoalRequest: MyGoalRequest
    ): Response<Unit>

    @DELETE("goals/{goalId}")
    suspend fun deleteMyGoalInfo(
        @Path("goalId") goalId: Int
    ): Response<Unit>
}