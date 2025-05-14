package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.dto.response.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.ReissueResponse

interface AuthRepository {
    suspend fun login(id: String, pwd: String): LoginResponse

    suspend fun logout(): Boolean

    suspend fun reissue(refreshToken: String): ReissueResponse

    suspend fun checkDuplId(userId: String): Boolean

    suspend fun join(userLoginId: String, userPw: String, userNickname: String, userBirth: String, userGender: Int): Boolean
}