package com.ssafy.fitmily_android.model.repositoryimpl

import com.ssafy.fitmily_android.domain.repository.NotificationRepository
import com.ssafy.fitmily_android.model.common.ApiResultHandler
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.request.notification.FcmRequest
import com.ssafy.fitmily_android.model.dto.response.notification.GetNotificationResponse
import com.ssafy.fitmily_android.model.dto.response.notification.UnReadNotificationResponse
import com.ssafy.fitmily_android.model.service.NotificationService
import jakarta.inject.Inject

class NotificationRepositoryImpl  @Inject constructor(
    private val notificationService: NotificationService
): NotificationRepository{
    override suspend fun sendFcmToken(userId: Int, fcmToken: String): Result<Unit> {
        return ApiResultHandler.handleApi {
            notificationService.sendFcmToken(FcmRequest(userId, fcmToken))
        }
    }

    override suspend fun getNotifications(): Result<GetNotificationResponse> {
        return ApiResultHandler.handleApi {
            notificationService.getNotifications()
        }
    }

    override suspend fun getUnReadNotificationInfo(): Result<UnReadNotificationResponse> {
        return ApiResultHandler.handleApi {
            notificationService.getUnReadNotificationInfo()
        }
    }
}