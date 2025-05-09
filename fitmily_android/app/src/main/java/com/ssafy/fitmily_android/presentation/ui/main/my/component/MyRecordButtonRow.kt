package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ssafy.fitmily_android.R

@Composable
fun MyRecordButtonRow(
    onHealthClick: () -> Unit,
    onGoalClick: () -> Unit,
    onExerciseClick: () -> Unit
) {
    Row(
    modifier = Modifier
    .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyRecordButton(
            iconRes = R.drawable.health,
            label = "건강 기록",
            onClick = onHealthClick,
            modifier = Modifier.weight(1f)
        )
        MyRecordButton(
            iconRes = R.drawable.health,
            label = "목표 설정",
            onClick = onGoalClick,
            modifier = Modifier.weight(1f)
        )
        MyRecordButton(
            iconRes = R.drawable.health,
            label = "운동 기록",
            onClick = onExerciseClick,
            modifier = Modifier.weight(1f)
        )
    }
}