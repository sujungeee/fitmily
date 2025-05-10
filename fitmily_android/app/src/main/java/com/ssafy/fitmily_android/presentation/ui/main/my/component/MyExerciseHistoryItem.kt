package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.my.ExerciseHistory
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyExerciseHistoryItem(
    history: ExerciseHistory
) {
    Column(
    ) {
        val valueText = if (history.unit != "km") {
            history.exerciseValue.toInt()
        } else {
            history.exerciseValue
        }

        // 이미지
        Image(
            painter = painterResource(id = history.iconRes),
            contentDescription = null,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 운동명
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = history.exerciseName,
                style = Typography.bodyLarge,
                color = mainBlack
            )
            if (history.exerciseName == "산책") {
                Text(
                    text = "더보기",
                    style = Typography.bodySmall,
                    color = mainDarkGray
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 칼로리, 거리 칩
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MyExerciseInfoChip(text = "${history.exerciseCalorie}kcal")
            MyExerciseInfoChip(text = "${valueText} ${history.unit}")
        }
    }
}