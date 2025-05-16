package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import jakarta.inject.Inject

class FcmUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userId: Int, fcmToken: String): Any {
        return authRepository.sendFcmToken(userId, fcmToken)
    }
}