package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalButton
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalRegisterExerciseImage
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalRegisterExerciseInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalRegisterExerciseValueInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyGoalTopBar
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray

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
            MyGoalRegisterExerciseImage(exerciseName = "산책")

            Spacer(modifier = Modifier.height(80.dp))

            // 운동 선택 영역
            MyGoalRegisterExerciseInputText(onClick = {/* TODO 바텀시트 띄우기 */})

            Spacer(modifier = Modifier.height(32.dp))

            // 운동 목표 영역
            MyGoalRegisterExerciseValueInputText()
        }
    }
}