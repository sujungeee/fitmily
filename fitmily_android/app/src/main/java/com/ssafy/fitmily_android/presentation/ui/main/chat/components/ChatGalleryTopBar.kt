package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R

@Composable
fun ChatGalleryTopBar(
    modifier: Modifier
    , onGalleryClose: () -> Unit
    , imageCount: Int
) {
    Row (
        modifier = modifier
        , verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton (
            // TODO: 갤러리 열기
            onClick = {

            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_icon)
                , contentDescription = "이미지 추가"
                , modifier = Modifier.size(24.dp)
            )
        }

        ChatGallerySendButton(
            { onGalleryClose() }
            , imageCount
        )
    }
}