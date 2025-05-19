package com.ssafy.fitmily_android.presentation.ui.main.my.goal

data class MyGoalRegisterUiState(
    val exerciseGoalName: String = "",
    val exerciseGoalValue: Float = 1f,
    val exerciseGoalValueInput: String = "",
    val myGoalSideEffect: MyGoalSideEffect? = null
)

sealed interface MyGoalSideEffect {
    data object NavigateToMy : MyGoalSideEffect
}