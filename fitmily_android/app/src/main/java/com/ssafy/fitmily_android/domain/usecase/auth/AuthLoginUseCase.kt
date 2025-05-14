package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.dto.response.LoginResponse
import jakarta.inject.Inject

class AuthLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String, pwd: String): LoginResponse {
        return authRepository.login(id, pwd)
    }
}