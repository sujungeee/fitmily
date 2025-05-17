package com.ssafy.fitmily_android.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.MainActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendNotificaton(message)
    }

    private fun sendNotificaton(remoteMessage: RemoteMessage) {
        val type = remoteMessage.data["type"] ?: "DEFAULT" // POKE, CHALLENGE, WALK, CHAT
        val channelId = type // CHAT
        val channelName = getChannelName(type) // 채팅 알림

        // notification channel
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, channelName, importance)

        // notification builder
        val notificationBuilder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= 26) {
            channel.description = "channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300)
            notificationBuilder = NotificationCompat.Builder(this, channelId)
        } else {
            notificationBuilder = NotificationCompat.Builder(this)
        }

        notificationManager.createNotificationChannel(channel)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val pendingIntent = createPendingIntent(remoteMessage)

        notificationBuilder
            .setSmallIcon(R.drawable.fitmily_logo)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun getChannelName(fcmType: String): String {
        return when (fcmType) {
            "CHAT" -> "채팅 알림"
            "POKE" -> "콕 찌르기 알림"
            "WALK" -> "산책 알림"
            "CHALLENGE" -> "챌린지 알림"
            else -> "기본 알림"
        }
    }

    private fun createPendingIntent(remoteMessage: RemoteMessage): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            for (key in remoteMessage.data.keys) {
                putExtra(key, remoteMessage.data.getValue(key))
            }
        }
        return PendingIntent.getActivity(this, 10101, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}