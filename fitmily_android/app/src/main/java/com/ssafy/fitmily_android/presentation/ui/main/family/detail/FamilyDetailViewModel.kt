package com.ssafy.fitmily_android.presentation.ui.main.family.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.family.FamilyDailyGetInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyDetailViewModel @Inject constructor(
    private val familyDailyGetInfoUseCase: FamilyDailyGetInfoUseCase
): ViewModel() {

    private val _familyDetailUiState = MutableStateFlow(FamilyDetailUiState())
    val familyDetailUiState: StateFlow<FamilyDetailUiState> = _familyDetailUiState

    fun getFamilyDailyInfo(familyId: Int, date: String) {

        val state = _familyDetailUiState.value

        Log.d("test1234", "getFamilyDailyInfo : members : ${state.familyDailyResponse?.members}")

        viewModelScope.launch {
            when (val result = familyDailyGetInfoUseCase(familyId = familyId, date = date)) {

                is Result.Success -> {
                    Log.d("test1234", "getFamilyCalendarInfo 호출 성공")

                    _familyDetailUiState.update { state ->
                        state.copy(
                            familyDailyResponse = result.data,
                            familyDetailSideEffect = FamilyDetailSideEffect.NavigateToFamilyExercise
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "getFamilyDailyInfo : Error 발생 : ${error?.code}")
                    Log.d("test1234", "getFamilyDailyInfo : Error 발생 : ${error?.message}")

                    Log.d("test1234", "getFamilyDailyInfo : Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "getFamilyDailyInfo : Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "getFamilyDailyInfo : 네트워크 에러")
                }
            }
        }
    }

    fun getExercisesByUserId(userId: Int): List<FamilyDailyExercise>? {
        return familyDetailUiState.value.familyDailyResponse?.members?.find { it.userId == userId }?.exercises
    }
}