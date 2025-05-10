package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.main.my.GoalItem
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalAddButton
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalItem
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyGoalTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray

@Composable
fun MyGoalScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray),
        topBar = { MyGoalTopBar(navController) },
        bottomBar = {
            MyGoalAddButton(
                onClick = { /* TODO 목표 추가 화면으로 가기 */ },
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 32.dp, start = 28.dp, end = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            val goals = listOf(
                GoalItem("산책", 0f, 3f, "km"),
                GoalItem("런지", 0f, 30f, "회"),
                GoalItem("산책", 0f, 3f, "km"),
                GoalItem("런지", 0f, 30f, "회"),
                GoalItem("산책", 0f, 3f, "km"),
                GoalItem("런지", 0f, 30f, "회"),
                GoalItem("산책", 0f, 3f, "km"),
                GoalItem("런지", 0f, 30f, "회"),
            )

            items (goals) { goal ->
                MyGoalItem(
                    goal = goal,
                    onEditClick = { /* TODO 수정 다이얼로그 띄우기 */ },
                    onDeleteClick = { /* TODO 삭제 이벤트 */ }
                )
            }
        }
    }
}