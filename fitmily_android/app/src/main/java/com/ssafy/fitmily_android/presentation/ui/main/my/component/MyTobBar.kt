package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyTobBar(
    profileImage: Painter,
    nickname: String,
    onNotificationClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(mainWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 32.dp, end = 28.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            Image(
                painter = profileImage,
                contentDescription = "가족 프로필 이미지",
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // 닉네임
            Text(
                text = nickname,
                color = mainBlack,
                style = Typography.headlineLarge,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            // 알람 아이콘
            IconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_bell),
                    contentDescription = "알림",
                    tint = Color.Black
                )
            }
        }
    }
}