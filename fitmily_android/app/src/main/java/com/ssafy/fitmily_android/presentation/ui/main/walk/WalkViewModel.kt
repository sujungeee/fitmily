package com.ssafy.fitmily_android.presentation.ui.main.walk

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.s3.S3UseCase
import com.ssafy.fitmily_android.domain.usecase.walk.GetPresignedUrlUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.GetWalkGoalExistUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.GetWalkHistoryUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.GetWalkPathUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.GetWalkingMemberUseCase
import com.ssafy.fitmily_android.domain.usecase.walk.PostWalkUseCase
import com.ssafy.fitmily_android.model.dto.request.walk.WalkEndRequest
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class WalkViewModel @Inject constructor(
    private val getWalkGoalExistUseCase: GetWalkGoalExistUseCase,
    private val getWalkPathUseCase: GetWalkPathUseCase,
    private val postWalkUseCase: PostWalkUseCase,
    private val getWalkingMemberUseCase: GetWalkingMemberUseCase,
    private val getPresignedUrlUseCase: GetPresignedUrlUseCase,
    private val s3UseCase: S3UseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState.asStateFlow()

    fun getWalkGoalExist() {
        viewModelScope.launch {
            val result = getWalkGoalExistUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    if(!data!!){
                        _uiState.value = _uiState.value.copy(tstMessage = "목표가 설정되어 있지 않습니다.")
                    }
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }

    fun getWalkPath(userId : Int) {
        viewModelScope.launch {
            val result = getWalkPathUseCase(userId)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(otherGpsList = data!!.path)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }

    fun getWalkingMembers(familyId : Int) {
        viewModelScope.launch {
            val result = getWalkingMemberUseCase(familyId)
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    _uiState.value = _uiState.value.copy(walkingFamilyList = data!!.member)
                },
                onError = { msg ->
                    _uiState.value = _uiState.value.copy(tstMessage = msg)
                }
            )
        }
    }


    suspend fun postWalk(walkEndRequest: WalkEndRequest, byteArray: ByteArray) {
        runCatching {
            var response = false
            var preSignedUrl = ""

            preSignedUrl = getPresignedUrl(System.currentTimeMillis().toString())
            response = uploadToS3(preSignedUrl, byteArray)

            if (response){
                val result = postWalkUseCase(walkEndRequest)
                ViewModelResultHandler.handle(
                    result = result,
                    onSuccess = { data ->
                        _uiState.value = _uiState.value.copy(tstMessage = "산책이 완료되었습니다.")
                    },
                    onError = { msg ->
                        _uiState.value = _uiState.value.copy(tstMessage = msg)
                    }
                )
            }
        }
    }

    suspend fun getPresignedUrl(fileName:String):String{
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                val result = getPresignedUrlUseCase(fileName, "image/jpeg")
                ViewModelResultHandler.handle(
                    result = result,
                    onSuccess = { data ->
                        continuation.resume(data!!)
                    },
                    onError = { msg ->
                        continuation.resumeWithException(Exception("발급 실패"))
                    }
                )
            }
        }
    }

    suspend fun uploadToS3(preSignedUrl: String, imageBytes: ByteArray) : Boolean {
        return suspendCoroutine { continuation ->
            viewModelScope.launch {
                val result = s3UseCase(preSignedUrl,
                    RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes))
                ViewModelResultHandler.handle(
                    result = result,
                    onSuccess = { data ->
                        continuation.resume(true)
                    },
                    onError = { msg ->
                        continuation.resumeWithException(Exception("업로드 실패"))
                    }
                )
            }
        }
    }



}