package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun ChatBottomBar (
    modifier : Modifier
    , onGalleryOpen: () -> Unit
) {
    var message by remember { mutableStateOf("") }

    Box (
        modifier = modifier
            .fillMaxWidth()
            .background(mainWhite)
            .height(IntrinsicSize.Min)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onGalleryOpen
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = "이미지 추가",
                    modifier = Modifier.size(24.dp)
                )
            }


            ChatInputTextField(
                modifier = Modifier.weight(1f), message, { message = it }
            )

            IconButton(
                onClick = {
                    // TODO: 전송
                    message = ""
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_icon),
                    contentDescription = "메시지 전송"
                )
            }
        }
    }
}