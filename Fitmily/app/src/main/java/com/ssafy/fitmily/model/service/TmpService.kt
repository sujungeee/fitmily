package com.ssafy.fitmily.model.service

import com.ssafy.fitmily.model.dto.response.TmpResponse

interface TmpService {
    suspend fun getUser(userId: Int) : TmpResponse
}