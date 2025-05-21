package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.FileRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.file.PresignedUrlRequest
import com.ssafy.fitmily_android.model.service.FileService
import jakarta.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileService: FileService
): FileRepository {
    override suspend fun getPresignedUrl(fileName: String, contentType: String): Result<String> {
        return ApiResultHandler.handleApi {
            fileService.getPresignedUrl(PresignedUrlRequest(fileName, contentType))
        }
    }
}