package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.model.dto.response.my.MyExerciseDto
import com.ssafy.fitmily_android.presentation.ui.main.my.ExerciseHistory
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun MyTodayExerciseHistory(
    totalExerciseCalorie: Int,
    histories: List<MyExerciseDto>,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "오늘의 운동 히스토리",
            color = mainBlack,
            style = Typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "총 $totalExerciseCalorie kcal",
            color = mainBlue,
            style = Typography.bodyLarge,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(20.dp))

        val rowItems = histories.chunked(2)
        rowItems.forEachIndexed { index, row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                row.forEach { history ->
                    Box(modifier = Modifier.weight(1f)) {
                        MyExerciseHistoryItem(history)
                    }
                }
                // 만약 아이템이 1개만 있는 Row라면, 빈 공간 채우기
                if (row.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if(index < rowItems.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}