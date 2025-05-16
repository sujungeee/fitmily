package com.ssafy.fitmily_android.domain.usecase.walk

import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.walk.WalkHistoryResponse
import javax.inject.Inject

class GetWalkHistoryUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(): Result<WalkHistoryResponse> {
        return walkRepository.getWalkHistory()
    }
}

