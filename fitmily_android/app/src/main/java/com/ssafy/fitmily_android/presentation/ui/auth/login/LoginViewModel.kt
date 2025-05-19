package com.ssafy.fitmily_android.presentation.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.domain.usecase.auth.AuthLoginUseCase
import com.ssafy.fitmily_android.domain.usecase.notification.SendFcmUseCase
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel_fitmily"
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authLoginUseCase: AuthLoginUseCase
    , private val sendFcmUseCase: SendFcmUseCase
) : ViewModel() {
    val authDataStore = MainApplication.getInstance().getDataStore()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun sendFcmToken(token: String) {
        viewModelScope.launch {
            val result = sendFcmUseCase(authDataStore.getUserId(), token)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    Log.d(TAG, "sendFcmToken: 토큰 전송 성공")
                },
                onError = { msg ->
                    Log.d(TAG, "sendFcmToken: 토큰 전송 실패")
                    Log.e(TAG, msg)
                }
            )
        }
    }

    fun login(id: String, pwd: String) {
        viewModelScope.launch {
            val result = authLoginUseCase(id, pwd)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    data!!.let {
                        if (it.familyId != null) {
                            authDataStore.setFamilyId(it.familyId)
                        }
                        authDataStore.setAccessToken(it.accessToken)
                        authDataStore.setRefreshToken(it.refreshToken)
                        authDataStore.setUserId(it.userId)
                        authDataStore.setUserNickname(it.userNickname)
                        authDataStore.setUserZodiacName(it.zodiacName)
                    }
                    _loginUiState.update {
                        it.copy(
                            loginResult = "SUCCESS"
                            , loginSideEffect = listOf(LoginSideEffect.NavigateToMain, LoginSideEffect.InitFCM)
                        )
                    }
                },
                onError = { msg ->
                    _loginUiState.update {
                        it.copy(loginResult = "FAIL")
                    }
                    Log.e(TAG, msg)
                }
            )
        }
    }
}