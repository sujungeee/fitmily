package com.ssafy.fitmily_android.presentation.ui.main.walk.history

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.walk.HistoryDto
import com.ssafy.fitmily_android.presentation.ui.main.walk.history.component.WalkHistoryItem
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun WalkHistoryScreen(
    navController: NavHostController,
    walkHistoryViewModel: WalkHistoryViewModel = hiltViewModel()
) {
    val walkHistoryUiState by walkHistoryViewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        walkHistoryViewModel.getWalkHistory()
    }
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
                text = "산책 기록",
                style = typography.headlineMedium,
            )

            Spacer(modifier = Modifier.size(10.dp))
        }

        LazyColumn(modifier = Modifier.padding(top = 32.dp)) {
             items(walkHistoryUiState.walkHistoryResponse.historyDto.size) { index ->
                WalkHistoryItem(
                    navController = navController,
                    item = walkHistoryUiState.walkHistoryResponse.historyDto[index],
                    onClick = {
                        val historyJson = Uri.encode(Gson().toJson(walkHistoryUiState.walkHistoryResponse.historyDto[index]))
                        navController.navigate("walk/detail/$historyJson")
                    }
                )
            }        }


    }
}



@Composable
@Preview(showSystemUi = true)
fun WalkHistoryScreenPreview() {
    WalkHistoryScreen(rememberNavController())
}
