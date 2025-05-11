package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyExerciseSelectBottomSheet
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseImage
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseValueInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalButton
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyGoalTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGoalRegisterScreen(
    navController: NavHostController
) {

    /* TODO 추후 UI STATE로 추출 */
    var selectedExercise by remember { mutableStateOf<String?>("런지") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

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
            MyExerciseInputText(
                onClick = {
                    showBottomSheet = true
                    Log.d("test1234", "운동 선택 클릭됌")
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 운동 목표 영역
            MyExerciseValueInputText()
        }

        if(showBottomSheet) {

            LaunchedEffect(Unit) {
                if(showBottomSheet) {
                    sheetState.show()
                }
                else {
                    sheetState.hide()
                }
            }

            MyExerciseSelectBottomSheet(
                selectedExercise = selectedExercise,
                onItemSelected = { selectedExercise ->
                    /*  TODO 선택된 운동 처리 */
                    showBottomSheet = false
                },
                onDismiss = { showBottomSheet = false },
                sheetState = sheetState
            )
        }
    }
}