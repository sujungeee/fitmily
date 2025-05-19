package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.my.MyExerciseResponse

interface MyExerciseRepository {

    suspend fun getMyExerciseInfo(): Result<MyExerciseResponse>

    suspend fun insertMyExerciseInfo(
        exerciseCount: Int,
        exerciseName: String,
        exerciseTime: Int
    ): Result<Unit>
}