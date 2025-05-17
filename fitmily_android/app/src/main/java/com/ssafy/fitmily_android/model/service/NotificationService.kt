package com.ssafy.fitmily_android.model.service

import com.ssafy.fitmily_android.model.dto.request.notification.FcmRequest
import com.ssafy.fitmily_android.model.dto.response.notification.Notification
import com.ssafy.fitmily_android.model.dto.response.notification.UnReadNotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationService {
    @POST("fcm/token")
    suspend fun sendFcmToken(
        @Body fcmRequest: FcmRequest
    ): Response<Unit>

    @GET("notifications")
    suspend fun getNotifications(): Response<List<Notification>>

    @GET("notifications/unread")
    suspend fun getUnReadNotificationInfo(): Response<UnReadNotificationResponse>
}