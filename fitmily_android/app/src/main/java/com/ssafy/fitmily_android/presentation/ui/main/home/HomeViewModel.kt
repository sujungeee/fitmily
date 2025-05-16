package com.ssafy.fitmily_android.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val createFamilyUseCase: CreateFamilyUseCase,
    private val getChallengeUseCase: GetChallengeUseCase,
    private val getDashboardUseCase: GetDashboardUseCase,
    private val getFamilyHealthUseCase: GetFamilyHealthUseCase,
    private val getFamilyUseCase: GetFamilyUseCase,
    private val joinFamilyUseCase: JoinFamilyUseCase,
    private val sendPokeUseCase: SendPokeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    fun createFamily(familyName: String) {
        viewModelScope.launch {
            val result = createFamilyUseCase(FamilyCreateRequest(familyName))
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(familyId = data.familyId)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstmessage = msg)
                }
            )
        }
    }

    fun getChallenge() {
        viewModelScope.launch {
            val result = getChallengeUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(challengeData = data)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstmessage = msg)
                }
            )
        }
    }

    fun getDashboard() {
        viewModelScope.launch {
            val result = getDashboardUseCase(familyId = 1, today = "2023-10-01")
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(dashBoardListData = data)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstmessage = msg)
                }
            )
        }
    }

    fun getFamilyHealth() {
        viewModelScope.launch {
            val result = getFamilyHealthUseCase(familyId = 1)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(familyHealthListData = data)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstmessage = msg)
                }
            )
        }
    }

    fun getFamily() {
        viewModelScope.launch {
            val result = getFamilyUseCase(familyId = 1)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(family = data)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstmessage = msg)
                }
            )
        }
    }

    fun joinFamily(familyCode: String) {
        viewModelScope.launch {
            val result = joinFamilyUseCase(FamilyJoinRequest(familyCode))
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(familyId = data.familyId)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstmessage = msg)
                }
            )
        }
    }

    fun sendPoke(userId: Int) {
        viewModelScope.launch {
            val result = sendPokeUseCase(userId)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(tstmessage = "000님을 콕 찔렀습니다.")
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstmessage = msg)
                }
            )
        }
    }
}


