package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.mygoal.MyGoalDeleteInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.mygoal.MyGoalGetInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.mygoal.MyGoalUpdateInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGoalViewModel @Inject constructor(
    private val myGoalGetInfoUseCase: MyGoalGetInfoUseCase,
    private val myGoalUpdateInfoUseCase: MyGoalUpdateInfoUseCase,
    private val myGoalDeleteInfoUseCase: MyGoalDeleteInfoUseCase
): ViewModel() {

    private val _myGoalUiState = MutableStateFlow(MyGoalUiState())
    val myGoalUiState: StateFlow<MyGoalUiState> = _myGoalUiState

    fun getMyGoalInfo() {
        viewModelScope.launch {

            when(val result = myGoalGetInfoUseCase()) {

                is Result.Success -> {

                    val data = result.data

                    Log.d("test1234", "getMyGoalInfo 호출 성공")
                    Log.d("test1234", "goal : ${data?.exerciseGoalProgress}")

                    _myGoalUiState.update { state ->
                        state.copy(
                            myGoalInfo = data
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "Error 발생 : ${error?.code}")
                    Log.d("test1234", "Error 발생 : ${error?.message}")

                    Log.d("test1234", "Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "Network 에러 발생")
                }
            }
        }
    }

    fun patchMyGoalInfo(goalId: Int, exerciseGoalValue: Float) {
        viewModelScope.launch {
            when(val result = myGoalUpdateInfoUseCase(goalId, exerciseGoalValue)) {

                is Result.Success -> {
                    Log.d("test1234", "patchMyGoalInfo 수정 성공")
                    getMyGoalInfo()
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "patchMyGoalInfo : Error 발생 : ${error?.code}")
                    Log.d("test1234", "patchMyGoalInfo : Error 발생 : ${error?.message}")

                    Log.d("test1234", "patchMyGoalInfo : Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "patchMyGoalInfo : Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "patchMyGoalInfo : 네트워크 에러")
                }
            }
        }
    }

    fun deleteMyGoalInfo(goalId: Int) {
        viewModelScope.launch {
            when(val result = myGoalDeleteInfoUseCase(goalId)) {

                is Result.Success -> {
                    Log.d("test1234", "deleteMyGoalInfo 삭제 성공")
                    getMyGoalInfo()
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "deleteMyGoalInfo : Error 발생 : ${error?.code}")
                    Log.d("test1234", "deleteMyGoalInfo : Error 발생 : ${error?.message}")

                    Log.d("test1234", "deleteMyGoalInfo : Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "deleteMyGoalInfo : Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "deleteMyGoalInfo : 네트워크 에러")
                }
            }
        }
    }
}