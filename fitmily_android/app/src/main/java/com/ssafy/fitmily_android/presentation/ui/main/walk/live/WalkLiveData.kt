package com.ssafy.fitmily_android.presentation.ui.main.walk.live

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


object WalkLiveData {

    // 서비스의 실행 성탸룰 나타내는 LiveData이다. 기본값은 false
    val isServiceRunning = MutableLiveData<Boolean>(false)

    private val _gpsList = MutableStateFlow<List<GpsDto>>(emptyList())
    val gpsList: StateFlow<List<GpsDto>> = _gpsList

    var otherData = MutableStateFlow(GpsDto(
        0.0, 0.0, System.currentTimeMillis().toString()
    ))
    // 위치 데이터를 위한 변수들
    var userId = 0
    var lat = 0.0
    var lon = 0.0
    var speed = 0.0
    var lastUpdatedTime: Long = 0
    var startedTime = System.currentTimeMillis()

    val shouldUpdateOtherGps = MutableLiveData<Boolean>(false)

    fun updateGpsList(newGps: GpsDto) {
        _gpsList.value = _gpsList.value + newGps
    }

    // GpsService를 시작하는 메서드
    fun startWalkLiveService(context: Context) {
        // 서비스가 실행 중이지 않은 경우에만 서비스를 시작한다.
        // 중복 동작과 ANR 방지 목적이다.


        if(!isServiceRun(context)) {
            val intent = Intent(context, WalkLiveService::class.java)
            ContextCompat.startForegroundService(context, intent)
            Toast.makeText(context, "Service Start", Toast.LENGTH_SHORT).show()
        }
    }

    // GpsService를 중지하는 메서드
    fun stopWalkLiveService(context: Context) {
        val intent = Intent(context, WalkLiveService::class.java)
        context.stopService(intent)
        if(WebSocketManager.isConnected) {
            WebSocketManager.unsubscribeStomp("/topic/walk/gps/${WebSocketManager.USERID}")
        }
            _gpsList.value = emptyList()

    }

    // 서비스의 실행 상태를 반환하는 메서드
    private fun isServiceRun(context: Context): Boolean {
        if (isServiceRunning.value == true) {
            return true
        }
        return false
    }
}