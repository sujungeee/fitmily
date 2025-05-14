package com.ssafy.fitmily_android.presentation.ui.main.family.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.family.detail.FamilyMemberStats
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun FamilyStatsItem(
    stats: FamilyMemberStats,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = mainWhite, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
            .clickable { onClick() }
    ) {
        Column(

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.my_unselected_icon),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier.background(stats.color, shape = CircleShape),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stats.name,
                    style = Typography.titleMedium,
                    color = mainBlack
                )
            }

            Spacer(Modifier.height(12.dp))

            // 첫 번째 Row: 값(진행률 원형, 칼로리, 시간)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 목표 진행률 - 원형
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    val size = 64.dp
                    CircularProgressIndicator(
                        progress = stats.progress / 100f,
                        strokeWidth = 8.dp,
                        color = mainBlue,
                        trackColor = mainGray,
                        modifier = Modifier.size(size)
                    )
                    Text(
                        text = "${stats.progress}%",
                        style = Typography.bodyLarge,
                        color = mainBlack
                    )
                }
                // 총 소모 칼로리
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${stats.kcal}kcal",
                        style = Typography.bodyLarge,
                        color = mainBlack
                    )
                }
                // 총 운동 시간
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stats.duration,
                        style = Typography.bodyLarge,
                        color = mainBlack
                    )
                }
            }

            // 두 번째 Row: 라벨
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "목표 진행률",
                        color = mainBlack,
                        style = Typography.bodyMedium
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "총 소모 칼로리",
                        style = Typography.bodyMedium,
                        color = mainBlack
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "총 운동 시간",
                        style = Typography.bodyMedium,
                        color = mainBlack
                    )
                }
            }
        }
    }
}