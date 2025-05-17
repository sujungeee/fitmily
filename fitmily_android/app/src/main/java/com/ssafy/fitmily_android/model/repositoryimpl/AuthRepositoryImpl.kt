package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.auth.FcmRequest
import com.ssafy.fitmily_android.model.dto.request.auth.JoinRequest
import com.ssafy.fitmily_android.model.dto.request.auth.LoginRequest
import com.ssafy.fitmily_android.model.dto.request.auth.ReissueRequest
import com.ssafy.fitmily_android.model.dto.response.auth.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.auth.ReissueResponse
import com.ssafy.fitmily_android.model.service.AuthService
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
): AuthRepository {
    override suspend fun login(userId: String, userPwd: String): Result<LoginResponse> {
        return ApiResultHandler.handleApi {
            authService.login(LoginRequest(userId, userPwd))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return ApiResultHandler.handleApi {
            authService.logout()
        }
    }

    override suspend fun reissue(refreshToken: String): Result<ReissueResponse> {
        return ApiResultHandler.handleApi {
            authService.reissue(ReissueRequest(refreshToken))
        }
    }

    override suspend fun checkDuplId(userId: String): Result<Boolean> {
        return ApiResultHandler.handleApi {
            authService.checkDuplId(userId)
        }
    }

    override suspend fun join(userId: String, userPwd: String, userNickname: String, userBirth: String, userGender: Int): Result<Unit> {
        return ApiResultHandler.handleApi {
            authService.join(JoinRequest(userId, userPwd, userNickname, userBirth, userGender))
        }
    }

    override suspend fun sendFcmToken(userId: Int, fcmToken: String): Result<Unit> {
        return ApiResultHandler.handleApi {
            authService.sendFcmToken(FcmRequest(userId, fcmToken))
        }
    }
}