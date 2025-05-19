package com.ssafy.fitmily_android.domain.usecase.walk

import androidx.compose.ui.autofill.ContentType
import com.ssafy.fitmily_android.domain.repository.FileRepository
import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.model.dto.response.walk.WalkEndResponse
import com.ssafy.fitmily_android.model.service.FileService
import javax.inject.Inject

class GetPresignedUrlUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(fileName:String, contentType: String): Result<Unit> {
        return  fileRepository.getPresignedUrl(fileName,contentType)
    }
}
