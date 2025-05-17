package com.ssafy.fitmily_android.presentation.ui.auth.join

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.auth.AuthCheckDuplIdUseCase
import com.ssafy.fitmily_android.domain.usecase.auth.AuthJoinUseCase
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "JoinViewModel_fitmily"
@HiltViewModel
class JoinViewModel @Inject constructor(
    private val authCheckDuplIdUseCase: AuthCheckDuplIdUseCase
    , private val authJoinUseCase: AuthJoinUseCase
): ViewModel(){
    private val _joinUiState = MutableStateFlow(JoinUiState())
    val joinUiState: StateFlow<JoinUiState> = _joinUiState

    fun checkDuplId(id: String) {
        viewModelScope.launch {
            val result = authCheckDuplIdUseCase(id)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _joinUiState.update { state ->
                        state.copy(isJoinAvailable = if (data == false) "Available" else "Not Available")
                    }
                },
                onError = { msg ->
                    _joinUiState.update { state ->
                        state.copy(isJoinAvailable = "Not Available")
                    }
                    Log.e(TAG, msg)
                }
            )
        }
    }

    fun join(id: String, pwd: String, nickname: String, birth: String, gender: Int) {
        viewModelScope.launch {
            val result = authJoinUseCase(id, pwd, nickname, birth, gender)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _joinUiState.update { state ->
                        state.copy(
                            joinResult = true
                            , joinSideEffect = JoinSideEffect.NavigateToLogin
                        )
                    }
                },
                onError = { msg ->
                    _joinUiState.update { state ->
                        state.copy(joinResult = false)
                    }
                    Log.e(TAG, msg)
                }
            )
        }
    }
}