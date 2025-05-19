package com.ssafy.fitmily_android.domain.usecase.s3

import com.ssafy.fitmily_android.domain.repository.S3Repository
import com.ssafy.fitmily_android.domain.repository.WalkRepository
import com.ssafy.fitmily_android.model.common.Result
import okhttp3.RequestBody
import javax.inject.Inject

class S3UseCase @Inject constructor(
    private val s3Repository: S3Repository
) {
    suspend operator fun invoke(url: String, file: RequestBody): Result<Unit> {
        return s3Repository.uploadFile(url, file)
    }
}
