package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.MyHealthRepository
import com.ssafy.fitmily_android.model.dto.request.my.MyHealthRequest
import com.ssafy.fitmily_android.model.dto.response.my.MyHealthResponse
import com.ssafy.fitmily_android.model.service.MyHealthService
import retrofit2.Response
import javax.inject.Inject

class MyHealthRepositoryImpl @Inject constructor(
    private val myHealthService: MyHealthService
): MyHealthRepository {
    override suspend fun getMyHealthInfo(): Response<MyHealthResponse> {
        return myHealthService.getMyHealthInfo()
    }

    override suspend fun insertMyHealthInfo(
        fiveMajorDiseases: List<String>,
        height: Float,
        otherDiseases: List<String>,
        weight: Float
    ): Response<Any> {
        val myHealthRequest = MyHealthRequest(
            fiveMajorDiseases = fiveMajorDiseases,
            height = height,
            otherDiseases = otherDiseases,
            weight = weight
        )
        return myHealthService.insertMyHealthInfo(myHealthRequest)
    }

    override suspend fun updateMyHealthInfo(
        fiveMajorDiseases: List<String>?,
        height: Float?,
        otherDiseases: List<String>?,
        weight: Float?
    ): Response<Any> {
        val myHealthRequest = MyHealthRequest(
            fiveMajorDiseases = fiveMajorDiseases,
            height = height,
            otherDiseases = otherDiseases,
            weight = weight
        )
        return myHealthService.updateMyHealthInfo(myHealthRequest)
    }
}