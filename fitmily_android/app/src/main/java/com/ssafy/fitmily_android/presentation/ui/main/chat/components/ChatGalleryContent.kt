package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatGalleryContent(
    modifier: Modifier
    , images: MutableList<Uri>
    , onDeleteImage: (Uri, Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
        , modifier = modifier
        , verticalArrangement = Arrangement.spacedBy(8.dp)
        , horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(images) { idx, image ->
            ChatGalleryItem(image, {
                onDeleteImage(image, idx)
            })
        }
    }
}