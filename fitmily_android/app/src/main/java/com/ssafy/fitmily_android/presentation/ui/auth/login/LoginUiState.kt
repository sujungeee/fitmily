package com.ssafy.fitmily_android.presentation.ui.auth.login

data class LoginUiState(
    val loginResult: String = ""
    , val loginSideEffect: List<LoginSideEffect>? = null
)

sealed interface LoginSideEffect {
    data object NavigateToMain : LoginSideEffect
    data object InitFCM: LoginSideEffect
}