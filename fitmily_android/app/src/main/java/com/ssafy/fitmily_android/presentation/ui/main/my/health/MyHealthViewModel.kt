package com.ssafy.fitmily_android.presentation.ui.main.my.health

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.myhealth.MyHealthGetInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.myhealth.MyHealthInsertInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.myhealth.MyHealthUpdateInfoUseCase
import com.ssafy.fitmily_android.presentation.ui.state.Mode
import com.ssafy.fitmily_android.presentation.ui.state.MyHealthSideEffect
import com.ssafy.fitmily_android.presentation.ui.state.MyHealthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.http.Tag
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
            runCatching {
                Log.d("test1234", "getMyHealthInfo 불림")
                myHealthGetInfoUseCase()
            }.onSuccess { response ->
                Log.d("test1234", "getMyHealthInfo 성공")
                if (response.body() == null) {
                    _myHealthUiState.update { state ->
                        state.copy(
                            mode = Mode.REGISTER,
                            isLoading = false
                        )
                    }
                } else {
                    _myHealthUiState.update { state ->
                        state.copy(
                            mode = Mode.MODIFIER,
                            height = response.body()!!.height.toString(),
                            weight = response.body()!!.weight.toString(),
                            selectedMajorDiseases = response.body()!!.fiveMajorDiseases,
                            otherDiseases = response.body()!!.otherDiseases,
                            isLoading = false
                        )
                    }
                }
            }.onFailure { e ->
                Log.d("test1234", "getMyHealthInfo 실패")
                _myHealthUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "정보 조회에 실패했습니다."
                    )
                }
            }
        }
    }

    fun submitMyHealthInfo() {

        val state = _myHealthUiState.value

        viewModelScope.launch {
            runCatching {

                // 등록 모드라면
                if(state.mode == Mode.REGISTER) {
                    if(state.isRegisterEnabled) {

                        Log.d("test1234", "등록모드다잇")
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
                    else {
                        Log.d("test1234", "등록모드인데 isRegisterEnabled가 false다")
                    }
                }
                // 수정 모드라면
                else {
                    if(state.isRegisterEnabled) {

                        Log.d("test1234", "수정모드다잇")
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
                    else {
                        Log.d("test1234", "수정모드인데 isRegisterEnabled가 false다")
                    }
                }
            }.onSuccess {
                _myHealthUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        myHealthSideEffect = MyHealthSideEffect.NavigateToMy
                    )
                }
            }.onFailure { e ->
                _myHealthUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "등록에 실패"
                    )
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