package com.ssafy.fitmily.domain.repository

import com.ssafy.fitmily.model.dto.response.TmpResponse

interface TmpRepository {
    suspend fun getUser(userId: Int) : TmpResponse
}