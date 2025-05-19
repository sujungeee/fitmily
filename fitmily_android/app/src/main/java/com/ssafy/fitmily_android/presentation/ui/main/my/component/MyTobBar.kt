package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.presentation.ui.main.my.MyViewModel
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun MyTobBar(
    userZodiacName: String,
    color: Color,
    nickname: String,
    onNotificationClick: () -> Unit
    , myViewModel : MyViewModel = hiltViewModel()
) {
    val uiState by myViewModel.myUiState.collectAsStateWithLifecycle()
    val hasNewNotification = uiState.hasUnreadNotification

    LaunchedEffect (Unit) {
        myViewModel.getUnReadNotificationInfo()
    }

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
                painter = painterResource(ProfileUtil().typeToProfile(userZodiacName) ?: R.drawable.my_selected_icon),
                contentDescription = "가족 프로필 이미지",
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // 닉네임
            Text(
                text = nickname,
                color = mainBlack,
                style = Typography.headlineLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            // 알람 아이콘
            Box (
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_bell),
                        contentDescription = "알림",
                        tint = Color.Black
                    )
                }

                if (hasNewNotification) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .offset(x = (-8).dp, y = 8.dp)
                            .background(Color.Red, shape = CircleShape)
                    )
                }
            }
        }
    }
}