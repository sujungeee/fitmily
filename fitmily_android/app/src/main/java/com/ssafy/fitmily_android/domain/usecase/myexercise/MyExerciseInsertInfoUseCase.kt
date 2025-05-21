package com.ssafy.fitmily_android.domain.usecase.myexercise

import com.ssafy.fitmily_android.domain.repository.MyExerciseRepository
import com.ssafy.fitmily_android.model.common.Result
import javax.inject.Inject

class MyExerciseInsertInfoUseCase @Inject constructor(
    private val myExerciseRepository: MyExerciseRepository
) {
    suspend operator fun invoke(
        exerciseCount: Int,
        exerciseName: String,
        exerciseTime: Int
    ): Result<Unit> {
        return myExerciseRepository.insertMyExerciseInfo(
            exerciseCount = exerciseCount,
            exerciseName = exerciseName,
            exerciseTime = exerciseTime
        )
    }
}