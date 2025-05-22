package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.model.dto.response.home.FamilyDashboardDto
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun DashBoardItem(item: FamilyDashboardDto, onClickPoke: (Int) -> Unit) {
    val userColor = ProfileUtil().seqToColor(item.userFamilySequence)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(mainWhite, shape = RoundedCornerShape(16.dp))
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileItem(
                sequence = item.userFamilySequence,
                name = item.userNickname ?: "이름없음",
                animal = item.userZodiacName ?: "동물없음",
            )

            TextButton(
                onClick = { onClickPoke(item.userId) },
                colors = ButtonDefaults.buttonColors(
                    mainWhite
                )
            ) {
                Text(
                    modifier = Modifier.background(userColor, shape = RoundedCornerShape(100.dp)).padding(4.dp),
                    text = "콕 찌르기",
                    style = typography.bodyMedium,
                    color = mainWhite,
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            if (item.goals.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxHeight(0.4f).fillMaxWidth(),
                    text = "설정된 목표가 없습니다.",
                    style = typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(0.55f)
                    ) {
                        items(item.goals.size) { index ->
                            GoalItem(item.goals[index], userColor)
                        }
                    }
                    Spacer(Modifier.size(20.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)
                                .aspectRatio(1f)
                                .fillMaxSize(),
                            color = userColor,
                            trackColor = mainGray,
                            strokeWidth = 8.dp,
                            progress = item.totalProgressRate / 100f,
                        )

                        Text(
                            text = item.totalProgressRate.toString(),
                            style = typography.bodyMedium,
                        )

                    }

                }
            }
        }
    }
}