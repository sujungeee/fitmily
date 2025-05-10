package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseImage
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseValueInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalButton
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyGoalTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray

@Composable
fun MyGoalRegisterScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray),
        topBar = { MyGoalTopBar(navController) },
        bottomBar = {
            MyGoalButton(
                text = "추가하기",
                onClick = {
                    /* TODO 목표 리스트에 추가하기 */
                    navController.popBackStack()
                },
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // 운동 이미지
            MyExerciseImage(exerciseName = "산책")

            Spacer(modifier = Modifier.height(80.dp))

            // 운동 선택 영역
            MyExerciseInputText(onClick = {/* TODO 바텀시트 띄우기 */})

            Spacer(modifier = Modifier.height(32.dp))

            // 운동 목표 영역
            MyExerciseValueInputText()
        }
    }
}