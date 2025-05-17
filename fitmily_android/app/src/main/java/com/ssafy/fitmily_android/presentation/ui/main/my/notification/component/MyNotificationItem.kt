package com.ssafy.fitmily_android.presentation.ui.main.my.notification.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.Notification
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.ibmFontFamily
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyNotificationItem (
    notification: Notification
    , navController : NavHostController
) {
    val notificationType = NotificationType.from(notification.type)

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(mainWhite)
            .padding(vertical = 12.dp)
    ) {
        Row () {
            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Image(
                painter = painterResource(notificationType!!.typeImage)
                , contentDescription = "typeImage"
                , modifier = Modifier
                    .size(width = 20.dp, height = 20.dp)
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = notificationType.typeName
                        , color = mainBlack
                        , style = TextStyle(
                            fontFamily = ibmFontFamily
                            , fontWeight = FontWeight.Medium
                            , fontSize = 12.sp
                        )
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = notification.date
                        , color = mainGray
                        , style = Typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                }

//                Spacer(
//                    modifier = Modifier.height(12.dp)
//                )

                Text(
                    text = notification.title
                    , color = mainBlack
                    , style = Typography.bodyMedium
                )

//                Spacer(
//                    modifier = Modifier.height(12.dp)
//                )

                if (notification.content.isNotEmpty()) {
                    Text(
                        text = notification.content
                        , color = mainBlack
                        , style = TextStyle(
                            fontFamily = ibmFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

enum class NotificationType (val typeName: String, val typeImage: Int) {
    POKE("콕 찌르기", R.drawable.sting_icon)
    , CHALLENGE("챌린지", R.drawable.challenge_icon)
    , WALK("산책", R.drawable.walk_icon);

    companion object {
        fun from(name: String): NotificationType? {
            return entries.find { it.name == name }
        }
    }
}