package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.presentation.ui.main.my.ExerciseHistory
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun MyTodayExerciseHistory(
    totalExerciseTime: String,
    histories: List<ExerciseHistory>
) {
    Column(
    ) {
        Text(
            text = "오늘의 운동 히스토리",
            color = mainBlack,
            style = Typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "총 $totalExerciseTime",
            color = mainBlue,
            style = Typography.bodyLarge,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(12.dp))

        histories.forEach { history ->
            MyExerciseHistoryItem(history)
            Spacer(Modifier.height(12.dp))
        }
    }
}