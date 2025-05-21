package com.ssafy.fitmily_android.domain.usecase.mygoal

import com.ssafy.fitmily_android.domain.repository.MyGoalRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.my.MyWeeklyProgressResponse
import javax.inject.Inject

class MyGoalWeeklyProgressInfoUseCase @Inject constructor(
    private val myGoalRepository: MyGoalRepository
) {
    suspend operator fun invoke(
        userId: Int
    ): Result<MyWeeklyProgressResponse> {
        return myGoalRepository.getMyWeeklyProgressInfo(userId)
    }
}