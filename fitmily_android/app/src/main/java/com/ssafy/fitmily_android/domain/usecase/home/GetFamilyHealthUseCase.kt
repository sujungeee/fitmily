package com.ssafy.fitmily_android.domain.usecase.home

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import javax.inject.Inject

class GetFamilyHealthUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(familyId: Int): Result<FamilyHealthResponse> {
        return homeRepository.getFamilyHealth(familyId)
    }
}

