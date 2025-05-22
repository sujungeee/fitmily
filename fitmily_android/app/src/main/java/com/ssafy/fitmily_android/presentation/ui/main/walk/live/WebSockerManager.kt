package com.ssafy.fitmily_android.presentation.ui.main.walk.live

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.datastore.dataStore
import com.google.gson.Gson
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.model.dto.response.walk.GpsDto
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

private const val TAG = "WebSockerManager"

object WebSocketManager {
    lateinit var stompClient: StompClient
    private val subscribeMap = mutableMapOf<String, io.reactivex.disposables.Disposable>()
    var isConnected = false

//    val url = "ws://192.168.137.1:8081/api/ws-connect"
    val url = "wss://k12d208.p.ssafy.io/api/ws-connect"
    var TOKEN = ""
    var USERID  = 0

    @SuppressLint("CheckResult")
    fun connectStomp(other :Boolean = false) {
        Log.d(TAG, "connectStomp: ${TOKEN}")
        var headerList: MutableList<StompHeader> = mutableListOf(
            StompHeader("Authorization", "Bearer ${TOKEN}")
        )
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

//        val heartBeatHandler = Handler(Looper.getMainLooper())
//        val heartBeatRunnable = object : Runnable {
//            override fun run() {
//                if (isConnected) {
//                    sendStompHeartBeat(stompClient)
//                    heartBeatHandler.postDelayed(this, 1000)
//                }
//            }
//        }
        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    isConnected = true
                    Log.d(TAG, "connectStomp: OPENED")
//                    heartBeatHandler.post(heartBeatRunnable)

                    // 이전에 구독했던 topic을 모두 재구독
                    for (topic in subscribeMap.keys.toList()) {
                        subscribeStomp(topic)
                    }

                    // 기본 구독
                    if (!other && !subscribeMap.containsKey("/topic/walk/gps/$USERID")) {
                        subscribeStomp("/topic/walk/gps/$USERID")
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
        fun subscribeStomp(topic: String = "/topic/walk/gps/$USERID") {
            if (subscribeMap.containsKey(topic)) {
                Log.d(TAG, "subscribeStomp: 이미 구독 중인 토픽입니다 -> $topic")
                return
            }

            val headers = listOf(StompHeader("Authorization", "Bearer $TOKEN"))
            val disposable = stompClient.topic(topic, headers)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ topicMessage ->
                    if (topic== "/topic/walk/gps/$USERID") {
                        topicMessage.payload?.let { payload ->
                            val message = Gson().fromJson(payload, GpsDto::class.java)
                            WalkLiveData.updateGpsList(message)
                        }
                    }else {
                        topicMessage.payload?.let { payload ->
                            val message = Gson().fromJson(payload, GpsDto::class.java)
                            WalkLiveData.otherData.value= message
                            Log.d(TAG, "waldd subscribeStomp: otherData -> ${message}")
                        }
                    }
                }, { error ->
                    Log.e(TAG, "subscribeStomp: 에러 발생 -> $topic", error)
                })

            subscribeMap[topic] = disposable
            Log.d(TAG, "subscribeStomp: 구독 완료 -> $topic")
        }

        fun unsubscribeStomp(topic: String) {
            subscribeMap[topic]?.dispose()
            val removed = subscribeMap.remove(topic)

            if (removed != null) {
                Log.d(TAG, "unsubscribeStomp: 해제 완료 -> $topic")
            } else {
                Log.d(TAG, "unsubscribeStomp: 해당 토픽은 구독 중이 아닙니다 -> $topic")
            }

            if (subscribeMap.isEmpty()) {
                Log.d(TAG, "unsubscribeStomp: 모든 구독 해제됨 → disconnect")
                disconnectStomp()
            }
        }

        fun disconnectStomp() {
            if (stompClient.isConnected) {
                stompClient.disconnect()
                isConnected = false
                subscribeMap.values.forEach { it.dispose() }
                subscribeMap.clear()
            }
        }

        fun getCurrentSubscribedTopics(): List<String> {
            return subscribeMap.keys.toList()
        }


    fun reconnectStomp() {
        if (!isConnected) {
            Handler(Looper.getMainLooper()).postDelayed({
                connectStomp()
            }, 3000)
        }
    }
//
//    fun sendStompHeartBeat(stompClient: StompClient) {
//        val heartBeat = HeartBeat(
//            type = "PING",
//        )
//
//        val jsonMessage = Gson().toJson(heartBeat)
//        try {
//            stompClient.send("/pub/chat/heartbeat", jsonMessage).subscribe()
//        } catch (e: Exception) {
//        }
//    }
//
//    data class HeartBeat(
//        val type: String,
//    )

}