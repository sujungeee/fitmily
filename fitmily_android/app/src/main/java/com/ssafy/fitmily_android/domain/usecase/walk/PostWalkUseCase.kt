package com.ssafy.fitmily_android.domain.usecase.walk

import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.WalkEndResponse
import javax.inject.Inject

class PostWalkUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(request: WalkEndRequest): Result<WalkEndResponse> {
        return walkRepository.postWalk(request)
    }
}
