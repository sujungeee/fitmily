package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.s3.UrlResponse

interface FileRepository {
    suspend fun getPresignedUrl(fileName: String, contentType: String): Result<UrlResponse>
}