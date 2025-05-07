package com.ssafy.fitmily_android.presentation.ui.main.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack

@Composable
fun MyExerciseStatusGraph(
    progress: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // 운동 현황 타이틀
        Text(
            text = "운동 현황",
            style = Typography.titleLarge,
            color = mainBlack
        )

        Spacer(Modifier.height(12.dp))

        // 운동 현황 그래프
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MyExerciseProgress(percent = progress)
        }
    }
}