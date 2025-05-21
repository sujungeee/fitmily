package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result

interface FileRepository {
    suspend fun getPresignedUrl(fileName: String, contentType: String): Result<String>
}