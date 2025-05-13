package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.dto.response.ReissueResponse
import jakarta.inject.Inject

class AuthReissueUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String): ReissueResponse {
        return authRepository.reissue(refreshToken)
    }
}