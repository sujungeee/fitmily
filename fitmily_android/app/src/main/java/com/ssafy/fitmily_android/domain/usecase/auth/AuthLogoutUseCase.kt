package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthLogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.logout()
    }
}