package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.HomeRepository
import com.ssafy.fitmily_android.domain.repository.S3Repository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.home.FamilyCreateRequest
import com.ssafy.fitmily_android.model.dto.request.home.FamilyJoinRequest
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyJoinResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyResponse
import com.ssafy.fitmily_android.model.dto.response.home.FamilyTodayResponse
import com.ssafy.fitmily_android.model.service.HomeService
import com.ssafy.fitmily_android.model.service.S3Service
import jakarta.inject.Inject
import okhttp3.RequestBody

class S3RepositoryImpl @Inject constructor(
    private val s3Service: S3Service
): S3Repository {


    override suspend fun uploadFile(url:String, file:RequestBody): Result<Unit> {
        return ApiResultHandler.handleApi {
            s3Service.uploadFile(url, file)
        }
    }


}