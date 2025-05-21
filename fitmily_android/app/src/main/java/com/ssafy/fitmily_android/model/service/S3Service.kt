package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.auth.JoinRequest
import com.ssafy.fitmily_android.model.dto.request.auth.LoginRequest
import com.ssafy.fitmily_android.model.dto.request.auth.ReissueRequest
import com.ssafy.fitmily_android.model.dto.response.auth.LoginResponse
import com.ssafy.fitmily_android.model.dto.response.auth.ReissueResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Url

interface S3Service {
    @PUT
    suspend fun uploadFile(
        @Url url: String,
        @Body file: RequestBody?)
    : Response<Unit>
}