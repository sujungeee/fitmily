package com.ssafy.fitmily_android.presentation.ui.main.walk.history.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.walk.HistoryDto
import com.ssafy.fitmily_android.presentation.ui.main.walk.history.WalkHistoryUiState
import com.ssafy.fitmily_android.util.DateUtil
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun WalkHistoryItem(
    navController: NavHostController,
    item: HistoryDto,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.routeImg,
            contentDescription = "산책 썸네일",
            modifier = Modifier
                .weight(0.8f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(2f)){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = item.nickname, style = typography.bodyLarge)
                Text(text = DateUtil().getFullDate(item.startTime), style = typography.bodySmall, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("거리", style = typography.bodySmall, color = Color.Gray)
                    Text("${item.distance}km", style = typography.bodyLarge)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("산책 시간", style = typography.bodySmall, color = Color.Gray)
                    Text(DateUtil().getDurationTime(item.startTime, item.endTime), style = typography.bodyLarge)
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .background(color = ProfileUtil().seqToColor(item.userFamilySequence), shape = CircleShape),
                )
            }
        }

    }
}