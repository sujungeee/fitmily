package com.ssafy.fitmily_android.presentation.ui.main.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.BuildConfig
import com.ssafy.fitmily_android.MainApplication
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
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeMemberDto
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

private const val TAG = "HomeViewModel"
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val createFamilyUseCase: CreateFamilyUseCase,
    private val getChallengeUseCase: GetChallengeUseCase,
    private val getDashboardUseCase: GetDashboardUseCase,
    private val getFamilyHealthUseCase: GetFamilyHealthUseCase,
    private val getFamilyUseCase: GetFamilyUseCase,
    private val joinFamilyUseCase: JoinFamilyUseCase,
    private val sendPokeUseCase: SendPokeUseCase,
    private val weatherGetInfoUseCase: WeatherGetInfoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadAllHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val familyId = uiState.value.familyId

            if (familyId != 0 && familyId != 100) {
                val getFamilyDeferred = launch { getFamily() }
                val getChallengeDeferred = launch { getChallenge() }
                val getDashboardDeferred = launch { getDashboard() }

                getFamilyDeferred.join()
                getChallengeDeferred.join()
                getDashboardDeferred.join()
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }


    fun getFamilyId(){
        viewModelScope.launch {
            val familyId = MainApplication.getInstance().getDataStore().getFamilyId()
            _uiState.update { it.copy(familyId = familyId) }
        }
    }

    fun createFamily(familyName: String) {
        viewModelScope.launch {
            val result = createFamilyUseCase(FamilyCreateRequest(familyName))
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    MainApplication.getInstance().getDataStore().setFamilyId(data!!.familyId)
                    _uiState.value = _uiState.value.copy(familyId = data!!.familyId)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
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
                    data?.let { challenge ->
                        if(challenge.participants != null) {
                            // rank 기준 정렬 후 최대 3명까지 추출
                            val sortedParticipants = challenge.participants
                                .sortedBy { it.rank }
                                .take(3)
                                .toMutableList()

                            // 부족한 등수만큼 "--" 채우기
                            val missingCount = 3 - sortedParticipants.size
                            repeat(missingCount) {
                                sortedParticipants.add(
                                    ChallengeMemberDto(
                                        distanceCompleted = 0.0,
                                        nickname = "--",
                                        familySequence = -1,
                                        zodiacName = "--",
                                        rank = sortedParticipants.size + 1,
                                        userId = -1
                                    )
                                )
                            }

                            _uiState.value = _uiState.value.copy(
                                challengeData = data.copy(
                                    challengeId = data!!.challengeId,
                                    progressPercentage = data!!.progressPercentage,
                                    startDate = data!!.startDate,
                                    targetDistance = data!!.targetDistance,
                                    participants = sortedParticipants
                                ),

                                )
                        } else {
                            // participants가 null일 경우에도 "--"로 3명 채워서 설정
                            val dummyParticipants = List(3) { index ->
                                ChallengeMemberDto(
                                    distanceCompleted = 0.0,
                                    nickname = "--",
                                    familySequence = -1,
                                    zodiacName = "--",
                                    rank = index + 1,
                                    userId = -1
                                )
                            }
                            _uiState.value = _uiState.value.copy(
                                challengeData = ChallengeResponse(
                                    challengeId = data!!.challengeId ?: 0,
                                    participants = dummyParticipants?: emptyList(),
                                    progressPercentage = data!!.progressPercentage?: 0,
                                    startDate = data!!.startDate?: "",
                                    targetDistance = data!!.targetDistance?: 0,
                                )
                            )
                        } ?: run {
                            // data 자체가 null일 경우에도 "--"로 3명 채워서 설정
                            val dummyParticipants = List(3) { index ->
                                ChallengeMemberDto()
                            }
                            _uiState.value = _uiState.value.copy(
                                challengeData = ChallengeResponse(
                                    challengeId = data!!.challengeId,
                                    participants = dummyParticipants,
                                    progressPercentage = data!!.progressPercentage,
                                    startDate = data!!.startDate,
                                    targetDistance = data!!.targetDistance,
                                )
                            )

                        }
                    }
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }


    fun getDashboard() {
        viewModelScope.launch {
            val result = getDashboardUseCase(familyId = uiState.value.familyId)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(
                        dashBoardListData = data!!)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }


    fun getFamily() {
        viewModelScope.launch {
            val result = getFamilyUseCase(familyId = uiState.value.familyId)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(family = data!!)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
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
                    MainApplication.getInstance().getDataStore().setFamilyId(data!!.familyId)
                    _uiState.value = _uiState.value.copy(familyId = data!!.familyId)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
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
                    _uiState.value = _uiState.value.copy(tstMessage = "000님을 콕 찔렀습니다.")
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }

    fun getWeatherInfo(lat: Double, lon: Double){
        viewModelScope.launch {
            ViewModelResultHandler.handle(
                result = weatherGetInfoUseCase(
                    lat = lat,
                    lon = lon,
                    exclude = "hourly,daily",
                    weatherApiKey = BuildConfig.WEATHER_API_KEY
                ),
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(weather = data)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }
}