package com.ssafy.fitmily_android.domain.usecase.notification

import com.ssafy.fitmily_android.domain.repository.NotificationRepository
import com.ssafy.fitmily_android.model.common.Result
import com.ssafy.fitmily_android.model.dto.response.notification.GetNotificationResponse
import jakarta.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val notificationRepository : NotificationRepository
) {
    suspend operator fun invoke(): Result<GetNotificationResponse> {
        return notificationRepository.getNotifications()
    }
}