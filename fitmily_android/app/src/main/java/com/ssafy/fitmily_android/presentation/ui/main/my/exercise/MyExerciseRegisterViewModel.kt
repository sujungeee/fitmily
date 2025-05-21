package com.ssafy.fitmily_android.presentation.ui.main.my.exercise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.myexercise.MyExerciseInsertInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyExerciseRegisterViewModel @Inject constructor(
    private val myExerciseInsertInfoUseCase: MyExerciseInsertInfoUseCase
): ViewModel() {

    private val _myExerciseRegisterUiState = MutableStateFlow(MyExerciseRegisterUiState())
    val myExerciseRegisterUiState: StateFlow<MyExerciseRegisterUiState> = _myExerciseRegisterUiState

    fun insertMyExerciseInfo() {
        viewModelScope.launch {

            val state = _myExerciseRegisterUiState.value

            Log.d("test1234", "insertMyExerciseInfo : exerciseName : ${state.exerciseName}")
            Log.d("test1234", "insertMyExerciseInfo : exerciseValue : ${state.exerciseValue}")
            Log.d("test1234", "insertMyExerciseInfo : exerciseTime : ${state.exerciseTime}")

            when (val result = myExerciseInsertInfoUseCase(
                                    exerciseName = state.exerciseName,
                                    exerciseCount = state.exerciseValue.toInt(),
                                    exerciseTime = state.exerciseTime
                               )
            ) {

                is Result.Success -> {
                    Log.d("test1234", "insertMyExerciseInfo 등록 성공")
                    _myExerciseRegisterUiState.update { state ->
                        state.copy(
                            myExerciseSideEffect = MyExerciseSideEffect.NavigateToMy
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "insertMyExerciseInfo : Error 발생 : ${error?.code}")
                    Log.d("test1234", "insertMyExerciseInfo : Error 발생 : ${error?.message}")

                    Log.d("test1234", "insertMyExerciseInfo : Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "insertMyExerciseInfo : Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "insertMyExerciseInfo : 네트워크 에러")
                }
            }
        }
    }

    fun updateExerciseName(name: String) {
        _myExerciseRegisterUiState.update { state ->
            state.copy(
                exerciseName = name
            )
        }
    }

    fun updateExerciseValue(value: String) {
        val floatValue = value.toFloatOrNull() ?: 1f

        _myExerciseRegisterUiState.update { state ->
            state.copy(
                exerciseValue = floatValue,
                exerciseValueInput = value
            )
        }
    }

    fun updateExerciseTime(time: String) {
        val intTime = time.toIntOrNull() ?: 1

        _myExerciseRegisterUiState.update { state ->
            state.copy(
                exerciseTime = intTime,
                exerciseTimeInput = time
            )
        }
    }

    fun consumeSideEffect() {
        _myExerciseRegisterUiState.update { state ->
            state.copy(
                myExerciseSideEffect = null
            )
        }
    }
}