package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.thirdBlue

@Composable
fun NewsItem(item: String, navController: NavHostController) {
    Column(
        modifier = Modifier.width(180.dp)
            .padding(bottom = 12.dp)
            .clickable {
                navController.navigate("home/news")
            }
    ){
        Image(
            modifier = Modifier
                .aspectRatio(1.8f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "item",
        )
        Spacer(Modifier.size(12.dp))
        Text(
            text = "$item,뉴스 제목은 다음과 같으며 다음다음 내용이 포함됩니다.",
            style = typography.bodyMedium,
            maxLines = 2
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = "뉴스 뉴스 내용은 다음과 같으며 다음다음 내용이 포함됩니뉴스 제목은 다음과 같으며 다음다음 내용이 포함됩니",
            style = typography.bodySmall,
            color = Color.Gray,
            maxLines = 2
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = "박대기 기자"
            , style = typography.bodySmall,
            modifier = Modifier.background(thirdBlue)
        )
    }
}