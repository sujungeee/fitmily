package com.ssafy.fitmily_android.domain.usecase.myexercise

import com.ssafy.fitmily_android.domain.repository.MyExerciseRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.my.MyExerciseResponse
import javax.inject.Inject

class MyExerciseGetInfoUseCase @Inject constructor(
    private val myExerciseRepository: MyExerciseRepository
) {
    suspend operator fun invoke(): Result<MyExerciseResponse> {
        return myExerciseRepository.getMyExerciseInfo()
    }
}