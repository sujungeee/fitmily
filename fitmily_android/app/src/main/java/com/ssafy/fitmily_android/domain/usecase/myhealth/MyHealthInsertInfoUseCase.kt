package com.ssafy.fitmily_android.domain.usecase.myhealth

import com.ssafy.fitmily_android.domain.repository.MyHealthRepository
import retrofit2.Response
import javax.inject.Inject

class MyHealthInsertInfoUseCase @Inject constructor(
    private val myHealthRepository: MyHealthRepository
) {
    suspend operator fun invoke(
        fiveMajorDiseases: List<String>,
        height: Float,
        otherDiseases: List<String>,
        weight: Float
    ): Response<Any> {
        return myHealthRepository.insertMyHealthInfo(
            fiveMajorDiseases = fiveMajorDiseases,
            height = height,
            otherDiseases = otherDiseases,
            weight = weight
        )
    }
}