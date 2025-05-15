package com.ssafy.fitmily_android.model.repositoryimpl

import com.google.gson.Gson
import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.LoginRequest
import com.ssafy.fitmily_android.model.dto.request.home.FamilyCreateRequest
import com.ssafy.fitmily_android.model.dto.request.home.FamilyJoinRequest
import com.ssafy.fitmily_android.model.dto.response.ErrorResponse
import com.ssafy.fitmily_android.model.dto.response.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyJoinResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse
import com.ssafy.fitmily_android.model.service.AuthService
import com.ssafy.fitmily_android.model.service.HomeService
import jakarta.inject.Inject
import java.io.IOException

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

    override suspend fun getDashboard(familyId: Int, today: String?): Result<FamilyTodayResponse> {
        return ApiResultHandler.handleApi {
            homeService.getDashboard(familyId, today)
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

    override suspend fun sendPoke(userId: Int): Result<Any> {
        return ApiResultHandler.handleApi {
            homeService.sendPoke(userId)
        }
    }
}