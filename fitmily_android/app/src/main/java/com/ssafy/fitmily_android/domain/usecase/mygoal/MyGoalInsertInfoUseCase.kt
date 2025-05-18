package com.ssafy.fitmily_android.domain.usecase.mygoal

import com.ssafy.fitmily_android.domain.repository.MyGoalRepository
import com.ssafy.fitmily_android.model.common.Result
import javax.inject.Inject

class MyGoalInsertInfoUseCase @Inject constructor(
    private val myGoalRepository: MyGoalRepository
) {
    suspend operator fun invoke(
        exerciseGoalName: String,
        exerciseGoalValue: Float
    ): Result<Unit> {
        return myGoalRepository.insertMyGoalInfo(
            exerciseGoalName = exerciseGoalName,
            exerciseGoalValue = exerciseGoalValue
        )
    }
}