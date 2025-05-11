package com.ssafy.fitmily_android.presentation.ui.auth.components

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
fun ActivateButton (
    onClick: () -> Unit
    , text: String
) {
    Button (
        onClick = onClick
        , modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 28.dp)
        , shape = RoundedCornerShape(16.dp)
        , colors = ButtonDefaults.buttonColors(mainBlue)
    ) {
        Text(
            text = text
            , color = mainWhite
            , style = Typography.bodyLarge
        )
    }
}