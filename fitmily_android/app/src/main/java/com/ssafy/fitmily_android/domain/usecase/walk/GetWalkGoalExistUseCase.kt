package com.ssafy.fitmily_android.domain.usecase.walk

import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.Result
import javax.inject.Inject

class GetWalkGoalExistUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return walkRepository.getWalkGoalExist()
    }
}


