package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatContentField (
    modifier : Modifier
) {
    LazyColumn (
        modifier = modifier
    ) {
        // TODO: delete
        item {
            ChatDateItem("2025.04.27 월요일")
        }

        item {
            OthersChatMessageItem(
                ChatMessage(
                    profileColor = "#FFD074BE",
                    profileIcon = "horse",
                    nickname = "김엄마",
                    message = "엄마zz 운동했다.zzzzzzzzzzzzz",
                    imageUrl = null,
                    unReadCount = 2,
                    date = "2025-04-27T16:27:00"
                )
            )
        }

        item {
            OthersChatMessageItem(
                ChatMessage(
                    profileColor = "#FFD074BE",
                    profileIcon = "horse",
                    nickname = "김엄마",
                    message = "엄마zz 운동했다.zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz",
                    imageUrl = null,
                    unReadCount = 2,
                    date = "2025-04-27T16:27:00"
                )
            )
        }

        item {
            OthersChatMessageItem(
                ChatMessage(
                    profileColor = "#FFD074BE",
                    profileIcon = "horse",
                    nickname = "김엄마",
                    message = "엄마zz 운동했다.zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz",
                    imageUrl = null,
                    unReadCount = 2,
                    date = "2025-04-27T16:27:00"
                )
            )
        }

        item {
            OthersChatMessageItem(
                ChatMessage(
                    profileColor = "#D074BE"
                    , profileIcon =  "horse"
                    , nickname =  "김엄마"
                    , message = null
                    , imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fe.kakao.com%2Ft%2Fopanchu-usagi&psig=AOvVaw1IN0kv3unWDXaKWS-F6cen&ust=1746772423775000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCJDQk7-gk40DFQAAAAAdAAAAABAI"
                    , unReadCount = 2
                    , date = "2025-04-27T16:27:00"
                )
            )
        }

        item {
            MyChatMessageItem(
                ChatMessage(
                    profileColor = "#FFEE00"
                    , profileIcon =  "rabbit"
                    , nickname =  "김수미"
                    , message = "엄마 잘했어요. 최고예요."
                    , imageUrl = null
                    , unReadCount = 3
                    , date = "2025-04-27T16:28:00"
                )
            )
        }

        item {
            MyChatMessageItem(
                ChatMessage(
                    profileColor = "#FFEE00"
                    , profileIcon =  "rabbit"
                    , nickname =  "김수미"
                    , message = null
                    , imageUrl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fe.kakao.com%2Ft%2Fopanchu-usagi&psig=AOvVaw1IN0kv3unWDXaKWS-F6cen&ust=1746772423775000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCJDQk7-gk40DFQAAAAAdAAAAABAI"
                    , unReadCount = 3
                    , date = "2025-04-27T16:28:00"
                )
            )
        }
    }
}

data class ChatMessage(
    val profileColor: String
    , val profileIcon: String
    , val nickname: String
    , val message: String?
    , val imageUrl: String?
    , val unReadCount: Int
    , val date: String
)