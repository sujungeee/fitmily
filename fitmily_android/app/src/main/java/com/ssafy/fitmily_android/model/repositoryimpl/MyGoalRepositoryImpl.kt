package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.MyGoalRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.dto.response.my.MyGoalResponse
import com.ssafy.fitmily_android.model.service.MyGoalService
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.my.MyGoalRequest
import com.ssafy.fitmily_android.model.dto.response.my.MyWeeklyProgressResponse
import javax.inject.Inject

class MyGoalRepositoryImpl @Inject constructor(
    private val myGoalService: MyGoalService
): MyGoalRepository {

    override suspend fun getMyGoalInfo(): Result<MyGoalResponse> {
        return ApiResultHandler.handleApi {
            myGoalService.getMyGoalInfo()
        }
    }

    override suspend fun insertMyGoalInfo(
        exerciseGoalName: String,
        exerciseGoalValue: Float
    ): Result<Unit> {
        return ApiResultHandler.handleApi {
            val myGoalRequest = MyGoalRequest(
                exerciseGoalName = exerciseGoalName,
                exerciseGoalValue = exerciseGoalValue
            )
            myGoalService.insertMyGoalInfo(myGoalRequest)
        }
    }

    override suspend fun patchMyGoalInfo(
        goalId: Int,
        exerciseGoalValue: Float
    ): Result<Unit> {
        return ApiResultHandler.handleApi {
            val myGoalRequest = MyGoalRequest(
                exerciseGoalValue = exerciseGoalValue
            )
            myGoalService.patchMyGoalInfo(
                goalId = goalId,
                myGoalRequest = myGoalRequest
            )
        }
    }

    override suspend fun deleteMyGoalInfo(goalId: Int): Result<Unit> {
        return ApiResultHandler.handleApi {
            myGoalService.deleteMyGoalInfo(goalId = goalId)
        }
    }

    override suspend fun getMyWeeklyProgressInfo(
        userId: Int
    ): Result<MyWeeklyProgressResponse> {
        return ApiResultHandler.handleApi {
            myGoalService.getMyWeeklyProgressInfo(userId)
        }
    }

}