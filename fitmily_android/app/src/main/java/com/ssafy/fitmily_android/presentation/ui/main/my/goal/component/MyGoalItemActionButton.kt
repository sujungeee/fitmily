package com.ssafy.fitmily_android.presentation.ui.main.my.goal.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyGoalItemActionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
        colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = Typography.bodyMedium,
            color = mainWhite
        )
    }
}