package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.FamilyRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.family.FamilyCalendarResponse
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyResponse
import com.ssafy.fitmily_android.model.service.FamilyService
import javax.inject.Inject

class FamilyRepositoryImpl @Inject constructor(
    private val familyService: FamilyService
): FamilyRepository {

    override suspend fun getFamilyCalendarInfo(
        familyId: Int,
        year: Int,
        month: String
    ): Result<FamilyCalendarResponse> {
        return ApiResultHandler.handleApi {
            familyService.getFamilyCalendarInfo(
                familyId = familyId,
                year = year,
                month = month
            )
        }
    }

    override suspend fun getFamilyDailyInfo(
        familyId: Int,
        date: String
    ): Result<FamilyDailyResponse> {
        return ApiResultHandler.handleApi {
            familyService.getFamilyDailyInfo(
                familyId = familyId,
                date = date
            )
        }
    }

}