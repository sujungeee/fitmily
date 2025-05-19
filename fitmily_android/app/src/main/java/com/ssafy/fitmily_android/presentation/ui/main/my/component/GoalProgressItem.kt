package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.model.dto.response.home.GoalDto
import com.ssafy.fitmily_android.model.dto.response.my.MyGoalDto
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray

@Composable
fun GoalProgressItem(goal: MyGoalDto) {

    val unit = when(goal.exerciseGoalName) {

        "산책" -> {
            "km"
        }

        else -> {
            "회"
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 운동 이름 + 현재 개수 + 목표 개수
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = goal.exerciseGoalName,
                color = mainBlack,
                style = Typography.bodyMedium
            )
            Spacer(Modifier.weight(1f))
            Text(
                text =
                    if(unit == "km")
                        "${goal.exerciseRecordValue} / ${goal.exerciseGoalValue} ${unit}"
                    else
                        "${goal.exerciseRecordValue.toInt()} / ${goal.exerciseGoalValue.toInt()} ${unit}",
                color = mainBlack,
                style = Typography.bodyMedium
            )
        }

        Spacer(Modifier.height(4.dp))

        // 선 프로그레스바
        LinearProgressIndicator(
            progress = (goal.exerciseRecordValue / goal.exerciseGoalValue).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = mainBlue,
            trackColor = mainGray
        )
    }
}