package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.dto.response.my.MyGoalResponse

interface MyGoalRepository {

    suspend fun getMyGoalInfo(): Result<MyGoalResponse>

    suspend fun insertMyGoalInfo(
        exerciseGoalName: String,
        exerciseGoalValue: Float
    ): Result<Any>

    suspend fun patchMyGoalInfo(
        goalId: Int,
        exerciseGoalName: String?,
        exerciseGoalValue: Float?
    ): Result<Any>

    suspend fun deleteMyGoalInfo(
        goalId: Int
    ): Result<Any>
}