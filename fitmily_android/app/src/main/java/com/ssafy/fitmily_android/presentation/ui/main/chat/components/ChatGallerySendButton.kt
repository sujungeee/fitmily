package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.util.DebugLogger
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.chat.ChatViewModel
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun ChatGallerySendButton (
    onGalleryClose: () -> Unit
    , images: MutableList<Uri>
    , imageCount: Int
    , chatViewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(mainBlue)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clickable {
                chatViewModel.sendMessage(
                    context
                    , type = "image"
                    , images = images.toList()
                )
                onGalleryClose()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = imageCount.toString()
                , style = Typography.bodyMedium
                , color = mainWhite
            )

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Text (
                text = "전송"
                , style = Typography.bodyMedium
                , color = mainWhite
            )

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.send_icon)
                , contentDescription = "이미지 전송"
                , modifier = Modifier.size(28.dp)
            )
        }
    }
}