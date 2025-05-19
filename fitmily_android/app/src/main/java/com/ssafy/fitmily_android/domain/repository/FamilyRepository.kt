package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.family.FamilyCalendarResponse
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyResponse

interface FamilyRepository {

    suspend fun getFamilyCalendarInfo(
        familyId: Int,
        year: Int,
        month: String
    ): Result<FamilyCalendarResponse>

    suspend fun getFamilyDailyInfo(
        familyId: Int,
        date: String
    ): Result<FamilyDailyResponse>
}