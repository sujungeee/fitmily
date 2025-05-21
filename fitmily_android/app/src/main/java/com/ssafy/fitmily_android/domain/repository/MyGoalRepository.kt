package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.dto.response.my.MyGoalResponse
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.my.MyWeeklyProgressResponse

interface MyGoalRepository {

    suspend fun getMyGoalInfo(): Result<MyGoalResponse>

    suspend fun insertMyGoalInfo(
        exerciseGoalName: String,
        exerciseGoalValue: Float
    ): Result<Unit>

    suspend fun patchMyGoalInfo(
        goalId: Int,
        exerciseGoalValue: Float
    ): Result<Unit>

    suspend fun deleteMyGoalInfo(
        goalId: Int
    ): Result<Unit>

    suspend fun getMyWeeklyProgressInfo(
        userId: Int
    ): Result<MyWeeklyProgressResponse>
}