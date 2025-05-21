package com.ssafy.fitmily_android.presentation.ui.main.home.profile.component

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.BuildConfig
import com.ssafy.fitmily_android.domain.usecase.home.CreateFamilyUseCase
import com.ssafy.fitmily_android.domain.usecase.home.GetChallengeUseCase
import com.ssafy.fitmily_android.domain.usecase.home.GetDashboardUseCase
import com.ssafy.fitmily_android.domain.usecase.home.GetFamilyHealthUseCase
import com.ssafy.fitmily_android.domain.usecase.home.GetFamilyUseCase
import com.ssafy.fitmily_android.domain.usecase.home.JoinFamilyUseCase
import com.ssafy.fitmily_android.domain.usecase.home.SendPokeUseCase
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.home.FamilyCreateRequest
import com.ssafy.fitmily_android.model.dto.request.home.FamilyJoinRequest
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import com.ssafy.fitmily_android.domain.usecase.weather.WeatherGetInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

@HiltViewModel
class FamilyProfileViewModel @Inject constructor(
    private val getFamilyHealthUseCase: GetFamilyHealthUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FamilyProfileUiState())
    val uiState: StateFlow<FamilyProfileUiState> = _uiState.asStateFlow()


    fun getFamilyHealth(familyId: Int) {
        viewModelScope.launch {
            val result = getFamilyHealthUseCase(familyId)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(familyHealthListData = data!!)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }
}