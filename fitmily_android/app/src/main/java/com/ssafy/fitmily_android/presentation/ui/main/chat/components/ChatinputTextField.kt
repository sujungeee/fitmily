package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainDarkGray

@Composable
fun ChatInputTextField(
    modifier : Modifier
    , value: String
    , onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .border(1.dp, mainBlack, RoundedCornerShape(16.dp))
            .background(Color.Transparent, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            singleLine = true,
            textStyle = Typography.bodyMedium.copy(color = Color.Black),
            visualTransformation = VisualTransformation.None,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "메시지 입력",
                        style = Typography.bodyMedium,
                        color = mainDarkGray
                    )
                }
                innerTextField()
            }
        )
    }
}