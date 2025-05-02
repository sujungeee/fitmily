package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.dto.response.TmpResponse

interface TmpRepository {
    suspend fun getUser(userId: Int) : TmpResponse
}