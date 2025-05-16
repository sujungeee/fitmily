package com.ssafy.fitmily_android.domain.usecase.home

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import javax.inject.Inject

class GetFamilyUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(familyId: Int): Result<FamilyResponse> {
        return homeRepository.getFamily(familyId)
    }
}
