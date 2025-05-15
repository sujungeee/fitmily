package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.WalkEndResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkHistoryResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkPathResponse

interface WalkRepository {

    suspend fun postWalk(request: WalkEndRequest): Result<WalkEndResponse>

    suspend fun getWalkHistory(): Result<WalkHistoryResponse>

    suspend fun getWalkPath(userId: Int): Result<WalkPathResponse>

    suspend fun getWalkGoalExist(): Result<Boolean>
}