package com.ssafy.fitmily_android.domain.usecase.home

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import javax.inject.Inject

class GetChallengeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<ChallengeResponse> {
        return homeRepository.getChallenge()
    }
}

