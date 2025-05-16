package com.ssafy.fitmily_android.presentation.ui.main.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ssafy.fitmily_android.BuildConfig
import com.ssafy.fitmily_android.domain.usecase.weather.WeatherGetInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.presentation.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.function.DoubleToIntFunction

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherGetInfoUseCase: WeatherGetInfoUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    fun getWeatherInfo(lat: Double, lon: Double) {
        viewModelScope.launch {
            Log.d("test1234", "getWeatherInfo 호출")
            _homeUiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            Log.d("test1234", "weatherApiKey : ${BuildConfig.WEATHER_API_KEY}")

            when(val result = weatherGetInfoUseCase(
                    lat = lat,
                    lon = lon,
                    exclude = "hourly,daily",
                    weatherApiKey = BuildConfig.WEATHER_API_KEY
                )
            ) {

                is Result.Success -> {
                    val data = result.data
                    Log.d("test1234", "getWeatherInfo 성공")
                    Log.d("test1234", "lat : ${data.lat} lon : ${data.lon}")
                    _homeUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            weather = data
                        )
                    }
                }

                is Result.Error -> {
                    Log.d("test1234", "getWeatherInfo 실패")
                    Log.d("test1234", "errorMessage : ${result.error?.message}")
                    Log.d("test1234", "errorMessage : ${result.error?.code}")
                    _homeUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.error?.message
                        )
                    }
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "네트워크 오류")
                    _homeUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "네트워크 오류"
                        )
                    }
                }
            }
        }
    }
}