package com.ssafy.fitmily_android.presentation.ui.main.family

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.family.FamilyCalendarGetInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val familyCalendarGetInfoUseCase: FamilyCalendarGetInfoUseCase
): ViewModel() {

    private val _familyUiState = MutableStateFlow(FamilyUiState())
    val familyUiState: StateFlow<FamilyUiState> = _familyUiState

    fun getFamilyCalendarInfo(familyId: Int, year: Int, month: String) {

        val state = _familyUiState.value

        Log.d("test1234", "getFamilyCalendarInfo : members : ${state.familyCalendarResponse?.members}")
        Log.d("test1234", "getFamilyCalendarInfo : calendar : ${state.familyCalendarResponse?.calendar}")

        viewModelScope.launch {
            when (val result = familyCalendarGetInfoUseCase(familyId = familyId, year = year, month = month)) {

                is Result.Success -> {
                    Log.d("test1234", "getFamilyCalendarInfo 호출 성공")

                    _familyUiState.update { state ->
                        state.copy(
                            familyCalendarResponse = result.data
                        )
                    }

                    val members = result.data?.members
                    Log.d("test1234", "getFamilyCalendarInfo members : $members")
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "getFamilyCalendarInfo : Error 발생 : ${error?.code}")
                    Log.d("test1234", "getFamilyCalendarInfo : Error 발생 : ${error?.message}")

                    Log.d("test1234", "getFamilyCalendarInfo : Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "getFamilyCalendarInfo : Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "getFamilyCalendarInfo : 네트워크 에러")
                }
            }
        }
    }
}