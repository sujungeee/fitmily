package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.common.Result
import jakarta.inject.Inject

class AuthCheckDuplIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String): Result<Boolean> {
        return authRepository.checkDuplId(id)
    }
}