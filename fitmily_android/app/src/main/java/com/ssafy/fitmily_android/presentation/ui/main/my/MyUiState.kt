package com.ssafy.fitmily_android.presentation.ui.main.my

import com.ssafy.fitmily_android.model.dto.response.my.MyExerciseResponse
import com.ssafy.fitmily_android.model.dto.response.my.MyGoalResponse
import com.ssafy.fitmily_android.model.dto.response.my.MyWeeklyProgressResponse

data class MyUiState (
    val logoutResult: Boolean = false,
    val mySideEffect: List<MySideEffect>? = null,
    val hasUnreadNotification: Boolean = false,
    val myGoalInfo: MyGoalResponse? = null,
    val myExerciseInfo: MyExerciseResponse? = null,
    val myExerciseTotalCalorie: Int = 0,
    val myGoalWeeklyProgressInfo: MyWeeklyProgressResponse? = null
)

sealed interface MySideEffect {
    data object NavigateToLogin : MySideEffect
    data object ClearAuthData: MySideEffect
    data object NavigateToWalk : MySideEffect
}