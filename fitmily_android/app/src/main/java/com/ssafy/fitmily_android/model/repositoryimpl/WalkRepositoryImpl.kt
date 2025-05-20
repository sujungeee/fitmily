package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.WalkEndResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkHistoryResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkPathResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkingFamilyResponse
import com.ssafy.fitmily_android.model.service.WalkService
import jakarta.inject.Inject

class WalkRepositoryImpl @Inject constructor(
    private val walkService: WalkService
): WalkRepository {
    override suspend fun postWalk(request: WalkEndRequest): Result<WalkEndResponse> {
        return ApiResultHandler.handleApi {
            walkService.postWalk(request)
        }
    }

    override suspend fun getWalkHistory(): Result<WalkHistoryResponse> {
        return ApiResultHandler.handleApi {
            walkService.getWalkHistory()
        }
    }

    override suspend fun getWalkPath(userId: Int): Result<WalkPathResponse> {
        return ApiResultHandler.handleApi {
            walkService.getWalkPath(userId)
        }
    }

    override suspend fun getWalkGoalExist(): Result<Boolean> {
        return ApiResultHandler.handleApi {
            walkService.getWalkGoalExist()
        }
    }

    override suspend fun getWalkingMembers(familyId: Int): Result<WalkingFamilyResponse> {
        return ApiResultHandler.handleApi {
            walkService.getWalkingMembers(familyId)
        }
    }


}