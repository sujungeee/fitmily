package com.ssafy.fitmily_android.presentation.ui.main.my

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.auth.AuthLogoutUseCase
import com.ssafy.fitmily_android.domain.usecase.myexercise.MyExerciseGetInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.mygoal.MyGoalGetInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.mygoal.MyGoalWeeklyProgressInfoUseCase
import com.ssafy.fitmily_android.domain.usecase.notification.GetUnReadNotificationInfoUseCase
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.my.MyWeeklyProgressResponse
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "MyViewModel_fitmily"
@HiltViewModel
class MyViewModel @Inject constructor(
    private val authLogoutUseCase: AuthLogoutUseCase,
    private val getUnReadNotificationInfoUseCase: GetUnReadNotificationInfoUseCase,
    private val myGoalGetInfoUseCase: MyGoalGetInfoUseCase,
    private val myExerciseGetInfoUseCase: MyExerciseGetInfoUseCase,
    private val myGoalWeeklyProgressInfoUseCase: MyGoalWeeklyProgressInfoUseCase
): ViewModel(){
    private val _myUiState = MutableStateFlow(MyUiState())
    val myUiState: StateFlow<MyUiState> = _myUiState

    fun logout() {
        viewModelScope.launch {
            val result = authLogoutUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _myUiState.update { state ->
                        state.copy(
                            logoutResult = true
                            , mySideEffect = listOf(MySideEffect.NavigateToLogin, MySideEffect.ClearAuthData)
                        )
                    }
                },
                onError = { msg ->
                    _myUiState.update {
                        it.copy(
                            logoutResult = false
                        )
                    }
                    Log.e(TAG, msg)
                }
            )
        }
    }

    fun getUnReadNotificationInfo() {
        viewModelScope.launch {
            val result = getUnReadNotificationInfoUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _myUiState.update {
                        it.copy(
                            hasUnreadNotification = data!!.hasUnreadNotification
                        )
                    }
                },
                onError = { msg ->
                    Log.e(TAG, msg)
                }
            )
        }
    }

    fun getMyGoalInfo() {
        viewModelScope.launch {

            when(val result = myGoalGetInfoUseCase()) {

                is Result.Success -> {

                    val data = result.data

                    Log.d("test1234", "getMyGoalInfo 호출 성공")
                    Log.d("test1234", "goal : ${data?.exerciseGoalProgress}")

                    _myUiState.update { state ->
                        state.copy(
                            myGoalInfo = data
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "getMyGoalInfo Error 발생 : ${error?.code}")
                    Log.d("test1234", "getMyGoalInfo Error 발생 : ${error?.message}")

                    Log.d("test1234", "getMyGoalInfo Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "getMyGoalInfo Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "getMyGoalInfo Network 에러 발생")
                }
            }
        }
    }

    fun getMyExerciseInfo() {
        viewModelScope.launch {
            when(val result = myExerciseGetInfoUseCase()) {

                is Result.Success -> {

                    val data = result.data

                    Log.d("test1234", "getMyExerciseInfo 호출 성공")
                    Log.d("test1234", "goal : ${data?.exercise}")

                    _myUiState.update { state ->
                        state.copy(
                            myExerciseInfo = data
                        )
                    }

                    calculateTotalCalorie()
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "getMyExerciseInfo Error 발생 : ${error?.code}")
                    Log.d("test1234", "getMyExerciseInfo Error 발생 : ${error?.message}")

                    Log.d("test1234", "getMyExerciseInfo Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "getMyExerciseInfo Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "getMyExerciseInfo Network 에러 발생")
                }
            }
        }
    }

    fun getMyGoalWeeklyProgressInfo(userId: Int) {
        viewModelScope.launch {
            when (val result = myGoalWeeklyProgressInfoUseCase(userId)) {

                is Result.Success -> {
                    val data = result.data

                    Log.d("test1234", "getMyGoalWeeklyProgressInfo 호출 성공")

                    _myUiState.update { state ->
                        state.copy(
                            myGoalWeeklyProgressInfo = data
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error
                    val exception = result.exception

                    Log.d("test1234", "getMyGoalWeeklyProgressInfo Error 발생 : ${error?.code}")
                    Log.d("test1234", "getMyGoalWeeklyProgressInfo Error 발생 : ${error?.message}")

                    Log.d("test1234", "getMyGoalWeeklyProgressInfo Exception 발생 : ${exception?.message}")
                    Log.d("test1234", "getMyGoalWeeklyProgressInfo Exception 발생 : ${exception?.stackTrace}")
                }

                is Result.NetworkError -> {
                    Log.d("test1234", "getMyGoalWeeklyProgressInfo Network 에러 발생")
                }
            }
        }
    }

    private fun calculateTotalCalorie() {
        val list = _myUiState.value.myExerciseInfo?.exercise ?: emptyList()

        val totalCalorie = list.sumOf { it.exerciseCalories }

        _myUiState.update { state ->
            state.copy(
                myExerciseTotalCalorie = totalCalorie
            )
        }
    }
}