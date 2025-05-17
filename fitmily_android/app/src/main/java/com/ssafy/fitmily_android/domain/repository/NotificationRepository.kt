package com.ssafy.fitmily_android.domain.repository

import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.notification.Notification
import com.ssafy.fitmily_android.model.dto.response.notification.UnReadNotificationResponse

interface NotificationRepository {
    suspend fun sendFcmToken(userId: Int, fcmToken: String): Result<Unit>

    suspend fun getNotifications(): Result<List<Notification>>

    suspend fun getUnReadNotificationInfo(): Result<UnReadNotificationResponse>
}