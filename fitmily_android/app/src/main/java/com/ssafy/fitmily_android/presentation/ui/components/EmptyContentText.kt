package com.ssafy.fitmily_android.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.ibmFontFamily
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun EmptyContentText (
    title: String
    , content: String
) {
    Box (
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title
                , style = Typography.bodyLarge
                , color = mainBlue
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = content, style = TextStyle(
                    fontFamily = ibmFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                , color = mainBlack
            )
        }
    }
}