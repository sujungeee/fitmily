package com.ssafy.fitmily_android.domain.usecase.s3

import com.ssafy.fitmily_android.domain.repository.FileRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.s3.UrlResponse
import javax.inject.Inject

class GetPresignedUrlUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(fileName:String, contentType: String): Result<UrlResponse> {
        return  fileRepository.getPresignedUrl(fileName,contentType)
    }
}
