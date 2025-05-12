package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainDarkGray

@Composable
fun MyLogout(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "로그아웃",
            color = mainDarkGray,
            style = Typography.bodyLarge
        )
    }
}