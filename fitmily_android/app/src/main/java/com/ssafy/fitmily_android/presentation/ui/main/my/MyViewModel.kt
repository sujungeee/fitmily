package com.ssafy.fitmily_android.presentation.ui.main.my

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.auth.AuthLogoutUseCase
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "MyViewModel_fitmily"
@HiltViewModel
class MyViewModel @Inject constructor(
    private val authLogoutUseCase: AuthLogoutUseCase
): ViewModel(){

    private val _myUiState = MutableStateFlow(MyUiState())
    val myUiState: StateFlow<MyUiState> = _myUiState

    fun logout() {
        viewModelScope.launch {
            val result = authLogoutUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _myUiState.update { state ->
                        state.copy(
                            logoutResult = true
                            , mySideEffect = listOf(MySideEffect.NavigateToLogin, MySideEffect.ClearAuthData)
                        )
                    }
                },
                onError = { msg ->
                    _myUiState.update {
                        it.copy(
                            logoutResult = false
                        )
                    }
                    Log.e(TAG, msg)
                }
            )
        }
    }
}