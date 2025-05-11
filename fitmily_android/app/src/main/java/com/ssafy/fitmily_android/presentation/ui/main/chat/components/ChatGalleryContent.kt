package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatGalleryContent(
    modifier: Modifier
    , images: List<Int>
    , onDeleteImage: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
        , modifier = modifier
        , verticalArrangement = Arrangement.spacedBy(8.dp)
        , horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { image ->
            ChatGalleryItem(image, { onDeleteImage(image) })
        }
    }
}