package com.ssafy.fitmily_android.domain.usecase.myhealth

import com.ssafy.fitmily_android.domain.repository.MyHealthRepository
import com.ssafy.fitmily_android.model.common.Result
import retrofit2.Response
import javax.inject.Inject

class MyHealthUpdateInfoUseCase @Inject constructor(
    private val myHealthRepository: MyHealthRepository
) {
    suspend operator fun invoke(
        fiveMajorDiseases: List<String>?,
        height: Float?,
        otherDiseases: List<String>?,
        weight: Float?
    ): Result<Any> {
        return myHealthRepository.updateMyHealthInfo(
            fiveMajorDiseases = fiveMajorDiseases,
            height = height,
            otherDiseases = otherDiseases,
            weight = weight
        )
    }
}