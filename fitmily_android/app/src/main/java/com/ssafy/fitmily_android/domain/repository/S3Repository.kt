package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.auth.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.auth.ReissueResponse
import okhttp3.RequestBody

interface S3Repository {
    suspend fun uploadFile(
        url: String,
        file: RequestBody
    ): Result<Unit>

}