package com.ssafy.fitmily_android.presentation.ui.main.my.goal.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyGoalAddButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button (
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 28.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(mainBlue),
        onClick = onClick,
    ) {
        Text(
            text = "목표 설정",
            style = Typography.bodyLarge,
            color = mainWhite
        )
    }
}