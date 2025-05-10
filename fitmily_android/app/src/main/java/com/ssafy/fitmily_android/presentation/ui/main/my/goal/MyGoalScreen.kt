package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.main.my.GoalItem
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
        bottomBar = { }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Text(
                "목표 설정 화면"
            )
        }
    }
}