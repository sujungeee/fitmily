package com.ssafy.fitmily_android.presentation.ui.main.my.exercise

data class MyExerciseRegisterUiState(
    val exerciseName: String = "",
    val exerciseValue: Float = 1f,
    val exerciseValueInput: String = "",
    val exerciseTime: Int = 1,
    val exerciseTimeInput: String = "",
    val myExerciseSideEffect: MyExerciseSideEffect? = null
)

sealed interface MyExerciseSideEffect {
    data object NavigateToMy: MyExerciseSideEffect
}