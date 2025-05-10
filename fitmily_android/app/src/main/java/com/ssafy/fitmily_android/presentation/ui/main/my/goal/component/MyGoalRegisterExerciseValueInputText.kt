package com.ssafy.fitmily_android.presentation.ui.main.my.goal.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
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
fun MyGoalRegisterExerciseValueInputText() {

    /* TODO 추후 UI STATE로 추출 */
    var inputValue by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
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
        BasicTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            singleLine = true,
            textStyle = Typography.bodyLarge.copy(color = mainBlack),
            decorationBox = { innerTextField ->
                if (inputValue.isEmpty()) {
                    Text(
                        text = "횟수 / 거리",
                        color = mainDarkGray,
                        style = Typography.bodyLarge
                    )
                }
                innerTextField()
            },
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}