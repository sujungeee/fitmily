package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.MyExerciseRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.my.MyExerciseRequest
import com.ssafy.fitmily_android.model.dto.response.my.MyExerciseResponse
import com.ssafy.fitmily_android.model.service.MyExerciseService
import javax.inject.Inject

class MyExerciseRepositoryImpl @Inject constructor(
    private val myExerciseService: MyExerciseService
): MyExerciseRepository {

    override suspend fun getMyExerciseInfo(): Result<MyExerciseResponse> {
        return ApiResultHandler.handleApi {
            myExerciseService.getMyExerciseInfo()
        }
    }

    override suspend fun insertMyExerciseInfo(
        exerciseCount: Int,
        exerciseName: String,
        exerciseTime: Int
    ): Result<Unit> {
        return ApiResultHandler.handleApi {
            val myExerciseRequest = MyExerciseRequest(
                exerciseCount = exerciseCount,
                exerciseName = exerciseName,
                exerciseTime = exerciseTime
            )
            myExerciseService.insertMyExerciseInfo(myExerciseRequest)
        }
    }
}