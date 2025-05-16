package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R

@Composable
fun ChatGalleryTopBar(
    modifier: Modifier
    , onGalleryClose: () -> Unit
    , images: MutableList<Uri>
    , imageCount: Int
) {
    Row (
        modifier = modifier
        , verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.End
    ) {
        ChatGallerySendButton(
            { onGalleryClose() }
            , images
            , imageCount
        )
    }
}