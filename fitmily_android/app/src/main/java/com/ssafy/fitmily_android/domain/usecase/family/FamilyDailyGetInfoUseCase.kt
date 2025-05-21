package com.ssafy.fitmily_android.domain.usecase.family

import com.ssafy.fitmily_android.domain.repository.FamilyRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyResponse
import javax.inject.Inject

class FamilyDailyGetInfoUseCase @Inject constructor(
    private val familyRepository: FamilyRepository
) {
    suspend operator fun invoke(
        familyId: Int,
        date: String
    ): Result<FamilyDailyResponse> {
        return familyRepository.getFamilyDailyInfo(
            familyId = familyId,
            date = date
        )
    }
}