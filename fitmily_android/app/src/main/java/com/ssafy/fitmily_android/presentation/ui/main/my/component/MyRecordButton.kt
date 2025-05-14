package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyRecordButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .background(mainWhite, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .background(mainBlue, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = label,
                color = mainWhite,
                style = Typography.bodyMedium
            )
        }
    }
}