package com.ssafy.fitmily.model.repositoryimpl

import com.ssafy.fitmily.domain.repository.TmpRepository
import com.ssafy.fitmily.model.dto.response.TmpResponse

class TmpRepositoryImpl() : TmpRepository {
    override suspend fun getUser(userId: Int): TmpResponse {
        // 서비스 호출
        return TmpResponse("d208", 0)
    }
}