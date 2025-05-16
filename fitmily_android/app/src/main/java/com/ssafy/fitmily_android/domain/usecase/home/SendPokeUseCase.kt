package com.ssafy.fitmily_android.domain.usecase.home

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.Result
import javax.inject.Inject

class SendPokeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(userId: Int): Result<Any> {
        return homeRepository.sendPoke(userId)
    }
}
