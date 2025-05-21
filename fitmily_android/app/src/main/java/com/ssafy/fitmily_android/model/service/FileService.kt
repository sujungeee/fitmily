package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.file.PresignedUrlRequest
import com.ssafy.fitmily_android.model.dto.response.s3.UrlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FileService {
    @POST("s3/upload-url")
    suspend fun getPresignedUrl(
        @Body presignedUrlRequest: PresignedUrlRequest
    ): Response<UrlResponse>
}