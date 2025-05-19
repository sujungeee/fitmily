package com.ssafy.fitmily_android.presentation.ui.main.my.health

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.myhealth.MyHealthGetInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.myhealth.MyHealthInsertInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.myhealth.MyHealthUpdateInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyHealthViewModel @Inject constructor(
    private val myHealthGetInfoUseCase: MyHealthGetInfoUseCase,
    private val myHealthInsertInfoUseCase: MyHealthInsertInfoUseCase,
    private val myHealthUpdateInfoUseCase: MyHealthUpdateInfoUseCase
): ViewModel() {

    private val _myHealthUiState = MutableStateFlow(MyHealthUiState())
    val myHealthUiState: StateFlow<MyHealthUiState> = _myHealthUiState

    fun getMyHealthInfo() {
        viewModelScope.launch {
            _myHealthUiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            when(val result = myHealthGetInfoUseCase()) {

                is Result.Success -> {

                    val data = result.data

                    if(data == null) {
                        _myHealthUiState.update { state ->
                            state.copy(
                                mode = Mode.REGISTER,
                                isLoading = false
                            )
                        }
                    }
                    else {
                        _myHealthUiState.update { state ->
                            state.copy(
                                mode = Mode.MODIFIER,
                                height = data.height.toString(),
                                weight = data.weight.toString(),
                                selectedMajorDiseases = data.fiveMajorDiseasesList,
                                otherDiseases = data.otherDiseasesList,
                                isLoading = false
                            )
                        }
                    }
                }

                is Result.Error -> {
                    _myHealthUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.error?.message
                        )
                    }
                }

                is Result.NetworkError -> {
                    _myHealthUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "네트워크 오류가 발생했습니다."
                        )
                    }
                }
            }
        }

    }

    fun submitMyHealthInfo() {
        val state = _myHealthUiState.value

        viewModelScope.launch {

            val result = if (state.mode == Mode.REGISTER) {
                if (state.isRegisterEnabled) {

                    Log.d("test1234", "등록모드")
                    Log.d("test1234", "height : ${state.height}")
                    Log.d("test1234", "weight : ${state.weight}")
                    Log.d("test1234", "fiveMajorDiseases : ${state.selectedMajorDiseases}")
                    Log.d("test1234", "otherDiseases : ${state.otherDiseases}")

                    myHealthInsertInfoUseCase(
                        fiveMajorDiseases = state.selectedMajorDiseases,
                        height = state.height.toFloat(),
                        otherDiseases = state.otherDiseases,
                        weight = state.weight.toFloat()
                    )
                }
                else return@launch
            }
            else {
                if (state.isRegisterEnabled) {

                    Log.d("test1234", "수정모드")
                    Log.d("test1234", "height : ${state.height}")
                    Log.d("test1234", "weight : ${state.weight}")
                    Log.d("test1234", "fiveMajorDiseases : ${state.selectedMajorDiseases}")
                    Log.d("test1234", "otherDiseases : ${state.otherDiseases}")

                    myHealthUpdateInfoUseCase(
                        fiveMajorDiseases = state.selectedMajorDiseases,
                        height = state.height.toFloat(),
                        otherDiseases = state.otherDiseases,
                        weight = state.weight.toFloat()
                    )
                }
                else return@launch
            }

            when (result) {
                is Result.Success -> {
                    Log.d("test1234", "건강정보 등록/수정 성공")
                    _myHealthUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            myHealthSideEffect = MyHealthSideEffect.NavigateToMy
                        )
                    }
                }

                is Result.Error -> {
                    Log.d("test1234", "건강정보 등록/수정 실패: ${result.error?.message}")
                    _myHealthUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.error?.message
                        )
                    }
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "네트워크 오류 발생")
                    _myHealthUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "네트워크 오류가 발생했습니다."
                        )
                    }
                }
            }
        }
    }

    fun checkMyHealthInfoInput() {
        val state = _myHealthUiState.value

        // 키 입력값 시 처리
        val isHeightValid = state.height.isNotBlank()

        // 몸무게 입력값 시 처리
        val isWeightValid = state.weight.isNotBlank()

        val isEnabled = isHeightValid && isWeightValid

        _myHealthUiState.update { state ->
            state.copy(
                isRegisterEnabled = isEnabled
            )
        }
    }

    fun onHeightChanged(newHeight: String) {
        _myHealthUiState.update { state ->
            state.copy(
                height = newHeight
            )
        }
        checkMyHealthInfoInput()
    }

    fun onWeightChanged(newWeight: String) {
        _myHealthUiState.update { state ->
            state.copy(
                weight = newWeight
            )
        }
        checkMyHealthInfoInput()
    }

    fun onTop5DiseaseSelected(disease: String) {
        val currentList = _myHealthUiState.value.selectedMajorDiseases

        val newList = if (currentList.contains(disease)) {
            currentList - disease
        } else {
            currentList + disease
        }
        _myHealthUiState.update { state ->
            state.copy(
                selectedMajorDiseases = newList
            )
        }
        checkMyHealthInfoInput()
    }

    fun onOtherDiseaseSelected(newChips: List<String>) {
        _myHealthUiState.update { state ->
            state.copy(
                otherDiseases = newChips
            )
        }
        checkMyHealthInfoInput()
    }



    fun consumeSideEffect() {
        _myHealthUiState.update { state ->
            state.copy(
                myHealthSideEffect = null
            )
        }
    }
}