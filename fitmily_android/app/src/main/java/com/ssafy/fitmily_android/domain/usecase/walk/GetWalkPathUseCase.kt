package com.ssafy.fitmily_android.domain.usecase.walk

import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.walk.WalkPathResponse
import javax.inject.Inject

class GetWalkPathUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(userId: Int): Result<WalkPathResponse> {
        return walkRepository.getWalkPath(userId)
    }
}


