package com.ssafy.fitmily_android.domain.usecase

import com.ssafy.fitmily_android.domain.repository.TmpRepository
import com.ssafy.fitmily_android.model.dto.response.TmpResponse

class TmpUseCase(
    private val tmpRepository : TmpRepository
) {
    suspend fun invoke(id: Int) : TmpResponse {
        return tmpRepository.getUser(id)
    }
}