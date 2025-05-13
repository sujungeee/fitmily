package com.ssafy.fitmily_android.presentation.ui.auth.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.auth.AuthCheckDuplIdUseCase
import com.ssafy.fitmily_android.domain.usecase.auth.AuthJoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val authCheckDuplIdUseCase: AuthCheckDuplIdUseCase
    , private val authJoinUseCase: AuthJoinUseCase
): ViewModel(){
    private val _joinUiState = MutableStateFlow(JoinUiState())
    val joinUiState: StateFlow<JoinUiState> = _joinUiState

    fun checkDuplId(id: String) {
        viewModelScope.launch {
            runCatching {
                authCheckDuplIdUseCase(id)
            }.onSuccess {
                // TODO: edit
                _joinUiState.update { state ->
                    state.copy(isJoinAvailable = if (it) "Available" else "Not Available")
                }
            }.onFailure {
                // TODO: add
            }
        }
    }

    fun join(id: String, pwd: String, nickname: String, birth: String, gender: Int) {
        viewModelScope.launch {
            runCatching {
                authJoinUseCase(id, pwd, nickname, birth, gender)
            }.onSuccess {
                // TODO: edit
                _joinUiState.update { state ->
                    state.copy(joinResult = it)
                }
            }.onFailure {
                // TODO: add
                _joinUiState.update { state ->
                    state.copy(joinResult = false)
                }
            }
        }
    }
}