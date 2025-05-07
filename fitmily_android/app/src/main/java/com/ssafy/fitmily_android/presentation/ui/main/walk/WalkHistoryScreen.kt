package com.ssafy.fitmily_android.presentation.ui.main.walk

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun WalkHistoryScreen(
) {
    Column(
        modifier = Modifier
            .padding(
                top = 32.dp,
                bottom = 24.dp,
                start = 28.dp,
                end = 28.dp
            )
    ) {
        Text(
            text = "산책",
            style = typography.headlineLarge,
        )

        LazyColumn(modifier = Modifier.padding(top = 32.dp)) {
             items(10) { index ->
                WalkHistoryItem(
                    thumbnailRes = R.drawable.ic_launcher_background,
                    name = "산책 ${index + 1}",
                    time = "00:30:00",
                    distance = "3.5km",
                    recordTime = "2023-10-01",
                    dotColor = if (index % 2 == 0) mainBlue else Color.Gray
                )
            }        }


    }
}

@Composable
fun WalkHistoryItem(
    thumbnailRes: Int,
    name: String,
    time: String,
    distance: String,
    recordTime: String,
    dotColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = thumbnailRes),
            contentDescription = "산책 썸네일",
            modifier = Modifier
                .weight(0.8f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(2f)){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = name, style = typography.bodyLarge)
                Text(text = recordTime, style = typography.bodySmall, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("거리", style = typography.bodySmall, color = Color.Gray)
                    Text(distance, style = typography.bodyLarge)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("산책 시간", style = typography.bodySmall, color = Color.Gray)
                    Text(time, style = typography.bodyLarge)
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .background(color = dotColor, shape = CircleShape),
                )
            }
        }

    }
}

@Composable
@Preview(showSystemUi = true)
fun WalkHistoryScreenPreview() {
    WalkHistoryScreen()
}
