package com.ssafy.fitmily_android.presentation.ui.main.my.notification

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.fitmily_android.domain.usecase.notification.GetNotificationsUseCase
import com.ssafy.fitmily_android.model.dto.response.notification.NotificationInfo
import com.ssafy.fitmily_android.util.DateUtil
import com.ssafy.fitmily_android.util.ViewModelResultHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

private const val TAG = "MyNotificationViewModel_fitmily"
@HiltViewModel
class MyNotificationViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase
): ViewModel() {
    private val _notifications = mutableStateOf<List<Notification>>(emptyList())
    val notifications: State<List<Notification>> = _notifications

    fun getNotifications() {
        viewModelScope.launch {
            val result = getNotificationsUseCase()
            ViewModelResultHandler.handle(
                result = result,
                onSuccess = { data ->
                    val list = data?.map {
                        Notification(
                            type = it.type
                            , date = DateUtil().getFullDate(it.receivedAt)
                            , title = getTitle(it.type, it.senderNickname, it.notificationInfo)
                            , content = getContent(it.type)
                        )
                    } ?: emptyList()
                    _notifications.value = list
                },
                onError = { msg ->
                    Log.e(TAG, msg)
                }
            )
        }
    }
}

fun getTitle(type: String, senderNickname: String, notificationInfo: NotificationInfo): String {
    when (type) {
        "POKE" -> {
            return "${senderNickname}님이 콕 찔렀습니다!"
        }
        "CHALLENGE" -> {
            val start = DateUtil().getMonthDay(notificationInfo.startDate)
            val end = DateUtil().getMonthDayPlusDays(notificationInfo.startDate, 7)
            return "$start ~ $end 가족 챌린지가 시작되었습니다!"
        }
        "WALK" -> {
            return "${senderNickname}님이 산책을 시작했어요!"
        }
    }
    return ""
}

fun getContent(type: String): String {
    when(type) {
        "CHALLENGE" -> {
            return "산책을 진행하여 등수를 높여보세요!"
        }
        "WALK" -> {
            return "열심히 산책하고 있는지 지켜볼까요?"
        }
    }
    return ""
}