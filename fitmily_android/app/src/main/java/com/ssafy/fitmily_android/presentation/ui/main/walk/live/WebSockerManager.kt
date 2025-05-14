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

    val url = "wss://j12d208.p.ssafy.io/ws/chat"

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

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
//        headerList.add(StompHeader("Authorization", "Bearer ${sharedPreferences.getAToken()}"))
//        headerList.add(StompHeader("roomId", roomId))
//        headerList.add(StompHeader("userId", sharedPreferences.getUser().userId.toString()))
//
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
        stompClient.connect()

    }

    @SuppressLint("CheckResult")
    fun subscribeStomp(topic: String) {
        stompClient.topic(topic).subscribe { topicMessage ->
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