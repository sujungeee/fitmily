package com.ssafy.fitmily.domain.usecase

import com.ssafy.fitmily.domain.repository.TmpRepository
import com.ssafy.fitmily.model.dto.response.TmpResponse

class TmpUseCase(
    private val tmpRepository : TmpRepository
) {
    suspend fun invoke(id: Int) : TmpResponse {
        return tmpRepository.getUser(id)
    }
}