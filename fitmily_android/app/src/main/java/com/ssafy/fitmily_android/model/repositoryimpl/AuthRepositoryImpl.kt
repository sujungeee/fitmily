package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.dto.request.FcmRequest
import com.ssafy.fitmily_android.model.dto.request.JoinRequest
import com.ssafy.fitmily_android.model.dto.request.LoginRequest
import com.ssafy.fitmily_android.model.dto.request.ReissueRequest
import com.ssafy.fitmily_android.model.dto.response.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.ReissueResponse
import com.ssafy.fitmily_android.model.service.AuthService
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
): AuthRepository {
    override suspend fun login(userId: String, userPwd: String): LoginResponse {
        return authService.login(LoginRequest(userId, userPwd))
    }

    override suspend fun logout(): Boolean {
        return authService.logout()
    }

    override suspend fun reissue(refreshToken: String): ReissueResponse {
        return authService.reissue(ReissueRequest(refreshToken))
    }

    override suspend fun checkDuplId(userId: String): Boolean {
        return authService.checkDuplId(userId)
    }

    override suspend fun join(userId: String, userPwd: String, userNickname: String, userBirth: String, userGender: Int): Boolean {
        return authService.join(JoinRequest(userId, userPwd, userNickname, userBirth, userGender))
    }

    override suspend fun sendFcmToken(userId: Int, fcmToken: String): Any {
        return authService.sendFcmToken(FcmRequest(userId, fcmToken))
    }
}