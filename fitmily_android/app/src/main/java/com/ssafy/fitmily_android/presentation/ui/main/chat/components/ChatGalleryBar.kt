package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.mainWhite
import kotlin.math.log

private const val TAG = "ChatGalleryBar_fitmily"
@Composable
fun ChatGalleryBar(
    modifier: Modifier
    , onGalleryClose: () -> Unit
    , images: MutableList<Uri>
) {
    BackHandler {
        onGalleryClose()
    }

    Box(
       modifier = modifier
           .fillMaxWidth()
           .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
           .background(mainWhite)
    ) {
        Column() {
            ChatGalleryTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 16.dp)
                    .padding(vertical = 8.dp)
                , { onGalleryClose() }
                , images
                , images.size
            )

            ChatGalleryContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .aspectRatio(1f)
                , images = images
                , onDeleteImage = { image, idx ->
                    images.removeAt(idx)
                }
            )
        }
    }
}