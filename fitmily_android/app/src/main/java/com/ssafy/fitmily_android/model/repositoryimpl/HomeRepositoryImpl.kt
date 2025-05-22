package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.home.FamilyCreateRequest
import com.ssafy.fitmily_android.model.dto.request.home.FamilyJoinRequest
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyJoinResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse
import com.ssafy.fitmily_android.model.service.HomeService
import jakarta.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
): HomeRepository {
    override suspend fun createFamily(request: FamilyCreateRequest): Result<FamilyJoinResponse> {
        return ApiResultHandler.handleApi {
            homeService.createFamily(request)
        }
    }


    override suspend fun joinFamily(request: FamilyJoinRequest): Result<FamilyJoinResponse> {
        return ApiResultHandler.handleApi {
            homeService.joinFamily(request)
        }
    }

    override suspend fun getFamily(familyId: Int): Result<FamilyResponse> {
        return ApiResultHandler.handleApi {
            homeService.getFamily(familyId)
        }
    }

    override suspend fun getDashboard(familyId: Int): Result<FamilyTodayResponse> {
        return ApiResultHandler.handleApi {
            homeService.getDashboard(familyId)
        }
    }

    override suspend fun getFamilyHealth(familyId: Int): Result<FamilyHealthResponse> {
        return ApiResultHandler.handleApi {
            homeService.getFamilyHealth(familyId)
        }
    }

    override suspend fun getChallenge(): Result<ChallengeResponse> {
        return ApiResultHandler.handleApi {
            homeService.getChallenge()
        }
    }

    override suspend fun sendPoke(userId: Int): Result<Unit> {
        return ApiResultHandler.handleApi {
            homeService.sendPoke(userId)
        }
    }
}