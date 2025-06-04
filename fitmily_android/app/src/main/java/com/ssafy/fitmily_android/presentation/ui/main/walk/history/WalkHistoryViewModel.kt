package com.ssafy.fitmily_android.presentation.ui.main.walk.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.walk.GetWalkGoalExistUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.GetWalkHistoryUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.GetWalkPathUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.PostWalkUseCase
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WalkHistoryViewModel @Inject constructor(
    private val getWalkHistoryUseCase: GetWalkHistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalkHistoryUiState())
    val uiState: StateFlow<WalkHistoryUiState> = _uiState.asStateFlow()

    fun getWalkHistory() {
        viewModelScope.launch {
            val result = getWalkHistoryUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(
                        walkHistoryResponse = data!!.copy(
                            walk = data.walk.reversed()
                        )
                    )
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }


}