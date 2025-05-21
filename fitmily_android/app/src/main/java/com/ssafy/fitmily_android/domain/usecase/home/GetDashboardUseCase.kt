package com.ssafy.fitmily_android.domain.usecase.home

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse
import javax.inject.Inject

class GetDashboardUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(familyId: Int): Result<FamilyTodayResponse> {
        return homeRepository.getDashboard(familyId)
    }
}
