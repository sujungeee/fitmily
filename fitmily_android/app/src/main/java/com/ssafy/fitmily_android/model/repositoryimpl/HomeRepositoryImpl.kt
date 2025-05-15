package com.ssafy.fitmily_android.model.repositoryimpl

import com.google.gson.Gson
import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.domain.repository.HomeRepository
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
        return try {
            val response = homeService.createFamily(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error(ErrorResponse(message = "응답 바디가 null입니다."))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    ErrorResponse(message = "에러 바디 파싱 실패")
                }
                Result.Error(errorResponse)
            }
        } catch (e: IOException) {
            Result.NetworkError
        } catch (e: Exception) {
            Result.Error(ErrorResponse(message = e.message ?: "알 수 없는 오류"))
        }
    }


    override suspend fun joinFamily(request: FamilyJoinRequest): Result<FamilyJoinResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFamily(familyId: Int): Result<FamilyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDashboard(familyId: Int, today: String?): Result<FamilyTodayResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFamilyHealth(familyId: Int): Result<FamilyHealthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getChallenge(): Result<ChallengeResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun sendPoke(userId: Int): Result<Any> {
        TODO("Not yet implemented")
    }
}