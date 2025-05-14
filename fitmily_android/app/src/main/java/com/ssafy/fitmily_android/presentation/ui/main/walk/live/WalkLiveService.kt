package com.ssafy.fitmily_android.presentation.ui.main.walk.live

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.MainActivity
import com.ssafy.fitmily_android.presentation.ui.main.walk.live.WalkLiveData.isServiceRunning


class WalkLiveService: Service() {
    private lateinit var walkLiveWorker: WalkLiveWorker
    val channelId = "walk_live_service_channel"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        try {
            startForeground(
                1,
                createChannel().build()
            )
            isServiceRunning.postValue(true)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Todo 웹소켓 시작
        if(!WebSocketManager.isConnected) {
            WebSocketManager.connectStomp()
        }



        // WalkLiveWorker를 시작, stompClient 보내서 send 할 수 있게끔
        walkLiveWorker = WalkLiveWorker(this)
        walkLiveWorker.startLocationUpdates()


        return START_STICKY
    }


    private fun createChannel(): NotificationCompat.Builder {

        // Android Oreo (API 26) 이상에서는 알림 채널을 생성해야 하기 때문에 작성
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            val channel = NotificationChannel(
                channelId,
                "Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // 알림을 탭하면 MainActivity로 이동하는 인텐트 생성
        val intent = Intent(this, MainActivity::class.java).apply {
        }

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // 알림을 구성하고 반환
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.walk_icon)
            .setContentTitle("산책 Live")
            .setContentText("산책 Live를 위해 위치정보를 수집하고 있습니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        walkLiveWorker.removeLocationUpdates()
        isServiceRunning.postValue(false)
    }

}