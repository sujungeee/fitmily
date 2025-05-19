package com.ssafy.fitmily_android.domain.usecase.mygoal

import com.ssafy.fitmily_android.domain.repository.MyGoalRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.my.MyGoalResponse
import javax.inject.Inject

class MyGoalGetInfoUseCase @Inject constructor(
    private val myGoalRepository: MyGoalRepository
) {
    suspend operator fun invoke(): Result<MyGoalResponse> {
        return myGoalRepository.getMyGoalInfo()
    }
}