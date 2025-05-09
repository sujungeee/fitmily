package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R

@Composable
fun ChatGalleryItem(
    image: Int
    , onDeleteImage: () -> Unit
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Image(
            painter = painterResource(image)
            , contentDescription = "갤러리 이미지"
            , contentScale = ContentScale.Crop
            , modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
            , alignment = Alignment.Center
        )

        IconButton (
            onClick = onDeleteImage
            , modifier = Modifier
                .align(Alignment.TopEnd)
                .size(width = 24.dp, height = 24.dp)
                .padding(top = 6.dp, end = 6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.delete_icon)
                , contentDescription = "갤러리 이미지 삭제"
                , modifier = Modifier.fillMaxSize()
            )
        }
    }
}