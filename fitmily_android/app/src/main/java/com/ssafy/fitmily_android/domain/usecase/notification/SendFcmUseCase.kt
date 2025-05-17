package com.ssafy.fitmily_android.domain.usecase.notification

import com.ssafy.fitmily_android.domain.repository.NotificationRepository
import com.ssafy.fitmily_android.model.common.Result
import jakarta.inject.Inject

class SendFcmUseCase @Inject constructor(
    private val notificationRepository : NotificationRepository
) {
    suspend operator fun invoke(userId: Int, fcmToken: String): Result<Unit> {
        return notificationRepository.sendFcmToken(userId, fcmToken)
    }
}