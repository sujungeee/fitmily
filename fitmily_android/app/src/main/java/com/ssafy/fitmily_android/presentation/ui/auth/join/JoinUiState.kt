package com.ssafy.fitmily_android.presentation.ui.auth.join

data class JoinUiState(
    val isJoinAvailable: String = "Not Initialized"
    , val joinResult: Boolean = false
    , val joinSideEffect: JoinSideEffect? = null
)

sealed interface JoinSideEffect {
    data object NavigateToLogin : JoinSideEffect
}