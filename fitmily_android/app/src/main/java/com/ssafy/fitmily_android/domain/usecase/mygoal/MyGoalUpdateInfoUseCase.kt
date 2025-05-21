package com.ssafy.fitmily_android.domain.usecase.mygoal

import com.ssafy.fitmily_android.domain.repository.MyGoalRepository
import com.ssafy.fitmily_android.model.common.Result
import javax.inject.Inject

class MyGoalUpdateInfoUseCase @Inject constructor(
    private val myGoalRepository: MyGoalRepository
) {
    suspend operator fun invoke(
        goalId: Int,
        exerciseGoalValue: Float
    ): Result<Unit> {
        return myGoalRepository.patchMyGoalInfo(
            goalId = goalId,
            exerciseGoalValue = exerciseGoalValue
        )
    }
}