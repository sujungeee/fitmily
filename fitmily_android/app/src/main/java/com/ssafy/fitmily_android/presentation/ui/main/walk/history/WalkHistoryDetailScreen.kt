package com.ssafy.fitmily_android.presentation.ui.main.walk.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.walk.HistoryDto
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.util.DateUtil

@Composable
fun WalkHistoryDetailScreen(
    navController:NavHostController,
    item: HistoryDto
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
        Row(modifier = Modifier.fillMaxWidth()
            ,horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    },
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "back",
            )
            Text(
                modifier = Modifier.padding(horizontal = 28.dp),
                text = "산책 결과",
                style = typography.headlineMedium,
            )

            Spacer(modifier = Modifier.size(10.dp))
        }


        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),){
            ProfileItem(sequence = item.userFamilySequence, name = item.nickname, animal = item.zodiacName)
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "님의 산책 결과",
                style = typography.bodyLarge,
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text("${item.distance}km", style = typography.titleMedium)
                Text("거리", style = typography.bodyMedium, color = Color.Gray)
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(mainGray)
            )
            Column(horizontalAlignment = Alignment.Start) {
                Text(DateUtil().getDurationTime(item.startTime, item.endTime), style = typography.titleMedium)
                Text("시간", style = typography.bodyMedium, color = Color.Gray)
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(mainGray)
            )
            Column(horizontalAlignment = Alignment.Start) {
                Text("${if(item.calories==0){"--"}else{item.calories}}kcal", style = typography.titleMedium)
                Text("칼로리", style = typography.bodyMedium, color = Color.Gray)
            }
        }
        AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 32.dp)
                    .clip(RoundedCornerShape(16.dp)),
                    model = item.routeImg,
                    contentScale = ContentScale.FillHeight,
                    contentDescription = "route",
            )
        Spacer(
            modifier = Modifier
                .weight(0.3f)
        )



    }
}

@Composable
@Preview(showSystemUi = true)
fun WalkHistoryDetailScreenPreview() {
    WalkHistoryDetailScreen(rememberNavController(),  HistoryDto())
}