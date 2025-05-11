package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun ChallengeCard(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .background(mainWhite, shape = RoundedCornerShape(16.dp))
            .padding(20.dp)
            .clickable {
                navController.navigate("home/challenge")
            }
    ) {
        Column {
            Text(
                text = "D-3",
                style = typography.bodyMedium,
                color = familyFirst
            )
            Text(
                text = "05.02 ~ 05.09",
                style = typography.bodyMedium,
            )
            Text(
                text = "총 30km 걷기",
                style = typography.bodyLarge,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "현재 순위",
                style = typography.bodyLarge,
                color = mainDarkGray
            )
            Spacer(Modifier.size(8.dp))
            LazyColumn {
                items(3) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${index + 1}위",
                            style = typography.bodyLarge,
                        )
                        Spacer(Modifier.size(8.dp))
                        ProfileItem()
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            SegmentedProgressBar(
                segments = listOf(
                    0.3f to Color.Blue,
                    0.2f to Color.Green,
                    0.5f to Color.Gray
                ),
            )
        }


    }
}

