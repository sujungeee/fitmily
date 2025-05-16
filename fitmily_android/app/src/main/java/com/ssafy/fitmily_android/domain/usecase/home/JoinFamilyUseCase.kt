package com.ssafy.fitmily_android.domain.usecase.home

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.home.FamilyJoinRequest
import com.ssafy.fitmily_android.model.dto.response.home.FamilyJoinResponse
import javax.inject.Inject

class JoinFamilyUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(request: FamilyJoinRequest): Result<FamilyJoinResponse> {
        return homeRepository.joinFamily(request)
    }
}

