package com.ssafy.fitmily_android.presentation.ui.main.walk.live

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

private const val TAG = "WebSockerManager"
object WebSocketManager {
    lateinit var stompClient: StompClient

    var subscribeList = mutableListOf<String>()
    var isConnected = false

    val url = "ws://192.168.100.130:8081/api/ws-connect"
    val TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJST0xFX1VTRVIiLCJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiaWF0IjoxNzQ3MjgzNjg3LCJleHAiOjE3NDcyODcyODd9.VEDTuuHFWil2ipdMN_ZvgXeQ079sVP-lvsiBLU1LyK0"
    var headerList: MutableList<StompHeader> = mutableListOf(
        StompHeader("Authorization", "Bearer ${TOKEN}")
    )

    var recentMessage = GpsDto(0.0, 0.0, "2023-10-01T12:00:00Z")

    private fun retryConnect() {
        if (!isConnected) {
            Handler(Looper.getMainLooper()).postDelayed({
                connectStomp()
            }, 3000)
        }
    }


    @SuppressLint("CheckResult")
    fun connectStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)


        val heartBeatHandler = Handler(Looper.getMainLooper())
        val heartBeatRunnable = object : Runnable {
            override fun run() {
                if (isConnected) {
//                    viewModel.sendStompHeartBeat(stompClient, roomId)
                    heartBeatHandler.postDelayed(this, 1000)
                }
            }
        }
//        왜있지
//        stompClient.disconnect()


        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    isConnected = true
                    Log.d(TAG, "connectStomp: OPENED")
//                    subscribeAll()
//                    heartBeatHandler.post(heartBeatRunnable)
                    if (WebSocketManager.stompClient.isConnected) {
                        WebSocketManager.subscribeStomp()
                    }
                }

                LifecycleEvent.Type.ERROR -> {
                    isConnected = false
                    Log.d(TAG, "connectStomp: ERROR")
                }

                LifecycleEvent.Type.CLOSED -> {
                    isConnected = false
                    Log.d(TAG, "connectStomp: CLOSED")
//                    heartBeatHandler.removeCallbacks(heartBeatRunnable)
                }

                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> reconnectStomp()

            }
        }
        stompClient.connect(headerList)

    }

    @SuppressLint("CheckResult")
    fun subscribeStomp() {
            stompClient.topic("/topic/walk/gps/1", headerList).subscribe { topicMessage ->
                Log.d(TAG, "subscribeStomp: 응답 ")
                topicMessage.payload?.let { payload ->
                    val message = Gson().fromJson(
                        topicMessage.getPayload(),
                        GpsDto::class.java
                    )

                    WalkLiveData.gpsList.value= WalkLiveData.gpsList.value?.plus(message) ?: listOf(message)
                    Log.d("WebSocketManager", "Received message: $message" )
                }
            }
    }

    @SuppressLint("CheckResult")
    fun subscribeStomp(topic: String){
        try {
            stompClient.topic(topic).subscribe { topicMessage ->
                Log.d(TAG, "subscribeStomp: 응답 ")
                topicMessage.payload?.let { payload ->
                    val message = Gson().fromJson(
                        topicMessage.getPayload(),
                        GpsDto::class.java
                    )

                    recentMessage = message
                    Log.d("WebSocketManager", "Received message: $message" )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error subscribing to topic: $topic", e)
        }
    }

    @SuppressLint("CheckResult")
    fun unsubscribeStomp(topic: String) {
        try {
            stompClient.topic(topic).unsubscribeOn(Schedulers.io())
            subscribeList.remove(topic)
        } catch (e: Exception) {
            Log.e(TAG, "Error unsubscribing from topic: $topic", e)
        }

        if (subscribeList.isEmpty()) {
            disconnectStomp()
        }
    }

    fun disconnectStomp() {
        if (stompClient.isConnected) {
            stompClient.disconnect()
            isConnected = false
        }
    }

    fun reconnectStomp() {
        if (!isConnected) {
            Handler(Looper.getMainLooper()).postDelayed({
                connectStomp()
            }, 3000)
        }
    }
}