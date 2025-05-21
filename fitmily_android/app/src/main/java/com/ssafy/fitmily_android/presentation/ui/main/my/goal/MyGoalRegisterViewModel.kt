package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.mygoal.MyGoalInsertInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGoalRegisterViewModel @Inject constructor(
  private val myGoalInsertInfoUseCase: MyGoalInsertInfoUseCase
): ViewModel() {

    private val _myGoalRegisterUiState = MutableStateFlow(MyGoalRegisterUiState())
    val myGoalRegisterUiState: StateFlow<MyGoalRegisterUiState> = _myGoalRegisterUiState


    fun insertMyGoalInfo() {
        viewModelScope.launch {

            val state = _myGoalRegisterUiState.value

            Log.d("test1234", "insertMyGoalInfo : exerciseGoalName : ${state.exerciseGoalName}")
            Log.d("test1234", "insertMyGoalInfo : exerciseGoalValue : ${state.exerciseGoalValue}")

            when(val result = myGoalInsertInfoUseCase(state.exerciseGoalName, state.exerciseGoalValue)) {

                is Result.Success -> {
                    Log.d("test1234", "insertMyGoalInfo 등록 성공")
                    _myGoalRegisterUiState.update { state ->
                        state.copy(
                            myGoalSideEffect = MyGoalSideEffect.NavigateToMy
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "insertMyGoalInfo : Error 발생 : ${error?.code}")
                    Log.d("test1234", "insertMyGoalInfo : Error 발생 : ${error?.message}")

                    Log.d("test1234", "insertMyGoalInfo : Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "insertMyGoalInfo : Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "insertMyGoalInfo : 네트워크 에러")
                }
            }
        }
    }

    fun updateExerciseGoalName(name: String) {
        _myGoalRegisterUiState.update { state ->
            state.copy(
                exerciseGoalName = name
            )
        }
    }

    fun updateExerciseGoalValue(value: String) {

        val floatValue = value.toFloatOrNull() ?: 1f

        _myGoalRegisterUiState.update { state ->
            state.copy(
                exerciseGoalValue = floatValue,
                exerciseGoalValueInput = value
            )
        }
    }

    fun consumeSideEffect() {
        _myGoalRegisterUiState.update { state ->
            state.copy(
                myGoalSideEffect = null
            )
        }
    }

    fun resetUiState() {
        _myGoalRegisterUiState.value = MyGoalRegisterUiState()
    }
}