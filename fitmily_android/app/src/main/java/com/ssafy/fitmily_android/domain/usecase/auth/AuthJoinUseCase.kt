package com.ssafy.fitmily_android.domain.usecase.auth

import com.ssafy.fitmily_android.domain.repository.AuthRepository
import com.ssafy.fitmily_android.model.common.Result
import jakarta.inject.Inject

class AuthJoinUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: String, pwd: String, nickname: String, birth: String, gender: Int): Result<Unit> {
        return authRepository.join(id, pwd, nickname, birth, gender)
    }
}