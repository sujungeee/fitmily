package com.ssafy.fitmily_android.domain.usecase.walk

import com.ssafy.fitmily_android.domain.repository.FileRepository
import com.ssafy.fitmily_android.model.common.Result
import javax.inject.Inject

class GetPresignedUrlUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(fileName:String, contentType: String): Result<String> {
        return  fileRepository.getPresignedUrl(fileName,contentType)
    }
}
