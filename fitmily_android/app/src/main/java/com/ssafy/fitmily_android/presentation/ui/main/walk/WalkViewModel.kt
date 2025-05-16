package com.ssafy.fitmily_android.presentation.ui.main.walk

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
class WalkViewModel @Inject constructor(
    private val getWalkGoalExistUseCase: GetWalkGoalExistUseCase,
    private val getWalkPathUseCase: GetWalkPathUseCase,
    private val postWalkUseCase: PostWalkUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState.asStateFlow()

    fun getWalkGoalExist() {
        viewModelScope.launch {
            val result = getWalkGoalExistUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    if(!data!!){
                        _uiState.value = _uiState.value.copy(tstMessage = "목표가 설정되어 있지 않습니다.")
                    }
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }

    fun getWalkPath(userId : Int) {
        viewModelScope.launch {
            val result = getWalkPathUseCase(userId)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(otherGpsList = data!!.path)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }

    fun postWalk(distance : Double, startTime : String, endTime : String, image : String) {
        viewModelScope.launch {
            val result = postWalkUseCase(WalkEndRequest(
                walkDistance = distance,
                walkStartTime = startTime,
                walkEndTime = endTime,
                walkRouteImg = image
            ))
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(tstMessage = "산책이 완료되었습니다.")
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }


}