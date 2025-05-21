package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.my.MyExerciseRequest
import com.ssafy.fitmily_android.model.dto.response.my.MyExerciseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyExerciseService {

    @GET("exercise")
    suspend fun getMyExerciseInfo(): Response<MyExerciseResponse>

    @POST("exercise")
    suspend fun insertMyExerciseInfo(
        @Body myExerciseRequest: MyExerciseRequest
    ): Response<Unit>
}