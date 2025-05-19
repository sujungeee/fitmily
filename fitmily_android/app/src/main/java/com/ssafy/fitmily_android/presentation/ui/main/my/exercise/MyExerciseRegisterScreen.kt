package com.ssafy.fitmily_android.presentation.ui.main.my.exercise

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyButton
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyExerciseSelectBottomSheet
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseImage
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseTimeInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseValueInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyExerciseTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyExerciseRegisterScreen(
    navController: NavHostController,
    myExerciseRegisterViewModel: MyExerciseRegisterViewModel = hiltViewModel()
) {

    val myExerciseRegisterUiState by myExerciseRegisterViewModel.myExerciseRegisterUiState.collectAsState()

    var selectedExercise by remember { mutableStateOf<String?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val inputValue = myExerciseRegisterUiState.exerciseValueInput
    val inputTime = myExerciseRegisterUiState.exerciseTimeInput

    LaunchedEffect(myExerciseRegisterUiState.myExerciseSideEffect) {
        when (myExerciseRegisterUiState.myExerciseSideEffect) {
            is MyExerciseSideEffect.NavigateToMy -> {
                navController.popBackStack()
                myExerciseRegisterViewModel.consumeSideEffect()
            }
            null -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TopBar 영역
            item {
                MyExerciseTopBar(
                    navController = navController
                )
            }

            // 운동 이미지
            item {
                Spacer(modifier = Modifier.height(80.dp))
                MyExerciseImage(
                    exerciseName = myExerciseRegisterUiState.exerciseName,
                    modifier = Modifier.padding(horizontal = 28.dp)
                )
            }


            // 운동 선택 영역
            item {
                Spacer(modifier = Modifier.height(80.dp))
                MyExerciseInputText(
                    selectedExercise = myExerciseRegisterUiState.exerciseName,
                    onClick = {
                        showBottomSheet = true
                    },
                    modifier = Modifier.padding(horizontal = 28.dp)
                )
            }

            // 운동 값 영역
            item {
                Spacer(modifier = Modifier.height(32.dp))
                MyExerciseValueInputText(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    onValueChange = { newValue ->
                        myExerciseRegisterViewModel.updateExerciseValue(newValue)
                    },
                    value = inputValue
                )
            }

            // 운동한 시간 영역
            item {
                Spacer(modifier = Modifier.height(32.dp))
                MyExerciseTimeInputText(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    onValueChange = { newValue ->
                        myExerciseRegisterViewModel.updateExerciseTime(newValue)
                    },
                    value = inputTime
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        MyButton(
            text = "기록하기",
            onClick = {
                myExerciseRegisterViewModel.insertMyExerciseInfo()
            },
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth()
        )
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
            mode = 1,
            selectedExercise = selectedExercise,
            onItemSelected = { exercise ->
                selectedExercise = exercise
                myExerciseRegisterViewModel.updateExerciseName(exercise)
                showBottomSheet = false
            },
            onDismiss = { showBottomSheet = false },
            sheetState = sheetState
        )
    }
}
