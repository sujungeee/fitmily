package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.response.TmpResponse

interface TmpService {
    suspend fun getUser(userId: Int) : TmpResponse
}