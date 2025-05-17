package com.ssafy.fitmily_android.presentation.ui.main.my

data class MyUiState (
    val logoutResult: Boolean = false
    , val mySideEffect: List<MySideEffect>? = null
)

sealed interface MySideEffect {
    data object NavigateToLogin : MySideEffect
    data object ClearAuthData: MySideEffect
}