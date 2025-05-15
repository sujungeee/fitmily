package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.dto.response.FcmResponse
import jakarta.inject.Inject

class FcmUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userId: String, fcmToken: String): FcmResponse {
        return authRepository.sendFcmToken(userId, fcmToken)
    }
}