package com.ssafy.fitmily_android.presentation.ui.main.my.goal.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainDarkGray

@Composable
fun MyExerciseInputText(
    onClick: () -> Unit
) {

    /* TODO 추후 UI STATE로 추출 */
    var selectedExercise by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val offset = 12.dp.toPx()

                drawLine(
                    color = mainDarkGray,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height + offset),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height + offset),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        Text(
            text = selectedExercise ?: "운동을 선택해 주세요.",
            color = if (selectedExercise == null) mainDarkGray else mainBlack,
            style = Typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Icon(
            painter = painterResource(id = R.drawable.arrow_bottom_icon),
            contentDescription = "운동 선택",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}