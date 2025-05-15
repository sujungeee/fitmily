package com.ssafy.fitmily_android.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.domain.usecase.auth.AuthLoginUseCase
import com.ssafy.fitmily_android.domain.usecase.auth.FcmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authLoginUseCase: AuthLoginUseCase
    , private val fcmUseCase: FcmUseCase
) : ViewModel() {
    val authDataStore = MainApplication.getInstance().getDataStore()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun sendFcmToken(token: String) {
        viewModelScope.launch {
            runCatching {
                fcmUseCase(authDataStore.getUserId(), token)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun login(id: String, pwd: String) {
        viewModelScope.launch {
            runCatching {
                authLoginUseCase(id, pwd)
            }.onSuccess {
                // TODO: edit
                _loginUiState.update { state ->
                    state.copy(
                        loginResult = true
                        , loginSideEffect = listOf (
                            LoginSideEffect.NavigateToMain
                            , LoginSideEffect.InitFCM
                        )

                    )
                }
                authDataStore.setAccessToken(it.accessToken)
                authDataStore.setRefreshToken(it.refreshToken)
                authDataStore.setUserId(it.userId)
                authDataStore.setUserNickname(it.userNickname)
                authDataStore.setUserProfileImg(it.userProfileImg)
                authDataStore.setUserColor(it.userColor)
            }.onFailure {
                // TODO: add
                _loginUiState.update {
                    it.copy(loginResult = false)
                }
            }
        }
    }
}