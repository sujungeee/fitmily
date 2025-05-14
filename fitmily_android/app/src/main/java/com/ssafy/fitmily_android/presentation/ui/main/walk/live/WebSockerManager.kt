package com.ssafy.fitmily_android.presentation.ui.main.walk.live

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.ssafy.fitmily_android.model.dto.GpsDto
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

private const val TAG = "WebSockerManager"
object WebSocketManager {
    lateinit var stompClient: StompClient

    var isConnected = false

    val url = "ws://192.168.100.130:8081/api/ws-connect"

    var headerList: MutableList<StompHeader> = mutableListOf()


    private fun retryConnect() {
        if (!isConnected) {
            Handler(Looper.getMainLooper()).postDelayed({
                connectStomp()
            }, 3000)
        }
    }


    @SuppressLint("CheckResult")
    fun connectStomp() {
        val TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJST0xFX1VTRVIiLCJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiaWF0IjoxNzQ3MjAxNTcyLCJleHAiOjE3NDcyMDI0NzJ9.wyvfrAnQzuln--KyHGm5_NR4R4cbEFATIollT_1PmgE"
        headerList.add(StompHeader("Authorization", "Bearer ${TOKEN}"))
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
                        WebSocketManager.subscribeStomp("/topic/walk/gps/1")
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
    fun subscribeStomp(topic: String) {

        stompClient.topic(topic, headerList).subscribe { topicMessage ->
            Log.d(TAG, "subscribeStomp: 응답 ")
            topicMessage.payload?.let { payload ->
                val message = Gson().fromJson(
                    topicMessage.getPayload(),
                    GpsDto::class.java
                )
                Log.d("WebSocketManager", "Received message: $message")


            }
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