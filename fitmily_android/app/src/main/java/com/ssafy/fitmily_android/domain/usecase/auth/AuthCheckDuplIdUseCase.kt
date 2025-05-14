package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import jakarta.inject.Inject

class AuthCheckDuplIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String): Boolean {
        return authRepository.checkDuplId(id)
    }
}