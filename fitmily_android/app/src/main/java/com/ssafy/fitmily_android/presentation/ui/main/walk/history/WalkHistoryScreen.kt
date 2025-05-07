package com.ssafy.fitmily_android.presentation.ui.main.walk.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.walk.history.component.WalkHistoryItem
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun WalkHistoryScreen(
    navController: NavHostController
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
                    navController = navController,
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
@Preview(showSystemUi = true)
fun WalkHistoryScreenPreview() {
    WalkHistoryScreen(rememberNavController())
}
