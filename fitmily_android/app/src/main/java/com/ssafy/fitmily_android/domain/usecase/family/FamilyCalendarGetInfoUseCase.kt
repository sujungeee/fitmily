package com.ssafy.fitmily_android.domain.usecase.family

import com.ssafy.fitmily_android.domain.repository.FamilyRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.family.FamilyCalendarResponse
import javax.inject.Inject

class FamilyCalendarGetInfoUseCase @Inject constructor(
    private val familyRepository: FamilyRepository
) {
    suspend operator fun invoke(
        familyId: Int,
        year: Int,
        month: String
    ): Result<FamilyCalendarResponse> {
        return familyRepository.getFamilyCalendarInfo(
            familyId = familyId,
            year = year,
            month = month
        )
    }
}