package com.ssafy.fitmily_android.presentation.ui.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ChallengeCard
import com.ssafy.fitmily_android.presentation.ui.main.home.component.DashBoardPager
import com.ssafy.fitmily_android.presentation.ui.main.home.component.NewsItem
import com.ssafy.fitmily_android.presentation.ui.main.home.component.WeatherCard
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(
                        top = 32.dp,
                    )
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    text = "Fitmily",
                    style = typography.titleLarge,
                    color = mainBlue
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "가족명",
                        style = typography.headlineLarge,
                    )
                    Button(
                        onClick = { navController.navigate("home/family") },
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "가족 건강 프로필", style = typography.bodyMedium
                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 28.dp),
                        text = "가족 건강 대시보드",
                        style = typography.titleLarge,
                    )
                    DashBoardPager(listOf("1", "2", "3", "4", "5"))
                }

                Column(
                    modifier = Modifier.padding(
                        top = 24.dp, start = 28.dp,
                        end = 28.dp
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = "산책 챌린지",
                        style = typography.titleLarge,
                    )
                    ChallengeCard(navController)
                }
                Column(
                    modifier = Modifier.padding(
                        top = 24.dp, start = 28.dp,
                        end = 28.dp
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = "오늘의 날씨",
                        style = typography.titleLarge,
                    )
                    WeatherCard()
                }
                Column(
                    modifier = Modifier.padding(
                        top = 24.dp, start = 28.dp,
                        end = 28.dp
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = "건강 뉴스",
                        style = typography.titleLarge,
                    )
                }
            }
        }
        itemsIndexed(listOf("1", "2", "3","4","5","6","7","8").chunked(2)) { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                item.forEachIndexed { rindex, ritem ->
                    NewsItem(ritem, navController)
                }
            }
        }
    }
}




@Composable
@Preview(showSystemUi = true)
fun HomeScreenPreview() {
        HomeScreen(rememberNavController())
}