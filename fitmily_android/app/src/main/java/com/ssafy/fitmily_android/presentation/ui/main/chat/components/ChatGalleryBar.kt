package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun ChatGalleryBar(
    modifier: Modifier
    , onGalleryClose: () -> Unit
) {
    BackHandler {
        onGalleryClose()
    }

    var imageCount by remember { mutableIntStateOf(-1) }
    // TODO: delete
    var images by remember { mutableStateOf(listOf(
        R.drawable.tmp_image, R.drawable.tmp_image2, R.drawable.tmp_image,
        R.drawable.tmp_image, R.drawable.tmp_image2, R.drawable.tmp_image,
        R.drawable.tmp_image, R.drawable.tmp_image, R.drawable.tmp_image)) }

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
                , imageCount
            )

            ChatGalleryContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .aspectRatio(1f)
                , images
                , { image ->
                    images = images.toMutableList().apply { remove(image) }
                }
            )
        }
    }
}