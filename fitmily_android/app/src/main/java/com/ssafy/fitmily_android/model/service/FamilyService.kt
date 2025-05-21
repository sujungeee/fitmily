package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.response.family.FamilyCalendarResponse
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FamilyService {

    @GET("family/{familyId}/calendar")
    suspend fun getFamilyCalendarInfo(
        @Path("familyId") familyId: Int,
        @Query("year") year: Int,
        @Query("month") month: String
    ): Response<FamilyCalendarResponse>

    @GET("family/{familyId}/daily")
    suspend fun getFamilyDailyInfo(
        @Path("familyId") familyId: Int,
        @Query("date") date: String
    ): Response<FamilyDailyResponse>
}