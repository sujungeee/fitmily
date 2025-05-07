package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray

@Composable
fun MyExerciseProgress(
    percent: Float,
) {
    val size = 120.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        CircularProgressIndicator(
            progress = percent / 100f,
            strokeWidth = 12.dp,
            color = mainBlue,
            trackColor = mainGray,
            modifier = Modifier.size(size)
        )
        Text(
            text = "${percent.toInt()}%",
            style = Typography.bodyLarge,
            color = mainBlack
        )
    }
}