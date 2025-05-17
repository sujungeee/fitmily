package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.auth.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.auth.ReissueResponse

interface AuthRepository {
    suspend fun login(userId: String, userPwd: String): Result<LoginResponse>

    suspend fun logout(): Result<Unit>

    suspend fun reissue(refreshToken: String): Result<ReissueResponse>

    suspend fun checkDuplId(userId: String): Result<Boolean>

    suspend fun join(userId: String, userPwd: String, userNickname: String, userBirth: String, userGender: Int): Result<Unit>

    suspend fun sendFcmToken(userId: Int, fcmToken: String): Result<Unit>
}