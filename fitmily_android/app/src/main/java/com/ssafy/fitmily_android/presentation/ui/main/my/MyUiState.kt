package com.ssafy.fitmily_android.presentation.ui.main.my

import com.ssafy.fitmily_android.model.dto.response.my.MyGoalResponse

data class MyUiState (
    val logoutResult: Boolean = false,
    val mySideEffect: List<MySideEffect>? = null,
    val hasUnreadNotification: Boolean = false,
    val myGoalInfo: MyGoalResponse? = null
)

sealed interface MySideEffect {
    data object NavigateToLogin : MySideEffect
    data object ClearAuthData: MySideEffect
}