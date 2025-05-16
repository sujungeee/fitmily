package com.ssafy.fitmily_android.presentation.ui.state

import com.ssafy.fitmily_android.model.service.MyHealthService

data class MyHealthUiState(
    val mode: Mode = Mode.REGISTER,
    val height: String = "",
    val weight: String = "",
    val selectedMajorDiseases: List<String> = emptyList(),
    val otherDiseases: List<String> = emptyList(),
    val isRegisterEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val myHealthSideEffect: MyHealthSideEffect? = null
)

enum class Mode {
    REGISTER,
    MODIFIER
}

sealed interface MyHealthSideEffect {
    data object NavigateToMy: MyHealthSideEffect
}