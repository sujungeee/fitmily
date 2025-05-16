package com.ssafy.fitmily_android.domain.usecase.myhealth

import com.ssafy.fitmily_android.domain.repository.MyHealthRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.my.MyHealthResponse
import retrofit2.Response
import javax.inject.Inject

class MyHealthGetInfoUseCase @Inject constructor(
    private val myHealthRepository: MyHealthRepository
) {
    suspend operator fun invoke(): Result<MyHealthResponse> {
        return myHealthRepository.getMyHealthInfo()
    }
}