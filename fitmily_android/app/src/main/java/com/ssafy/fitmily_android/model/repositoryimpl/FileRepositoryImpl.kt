package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.FileRepository
import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.WalkEndResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkHistoryResponse
import com.ssafy.fitmily_android.model.dto.response.walk.WalkPathResponse
import com.ssafy.fitmily_android.model.service.FileService
import com.ssafy.fitmily_android.model.service.WalkService
import jakarta.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileService: FileService
): FileRepository {

    override suspend fun getPresignedUrl(fileName: String, contentType: String): Result<Unit> {
        return ApiResultHandler.handleApi {
            fileService.getPresignedUrl(fileName, contentType)
        }
    }

}