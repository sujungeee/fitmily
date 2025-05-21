package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import android.app.Activity
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyButton
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyExerciseSelectBottomSheet
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseImage
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyExerciseValueInputText
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyGoalTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyGoalRegisterScreen(
    navController: NavHostController,
    myGoalRegisterViewModel: MyGoalRegisterViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val activity = context as? Activity
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    val myGoalRegisterUiState by myGoalRegisterViewModel.myGoalRegisterUiState.collectAsState()

    var selectedExercise by remember { mutableStateOf<String?>("런지") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val inputValue = myGoalRegisterUiState.exerciseGoalValueInput

    LaunchedEffect(myGoalRegisterUiState.myGoalSideEffect) {
        when (myGoalRegisterUiState.myGoalSideEffect) {
            is MyGoalSideEffect.NavigateToMy -> {
                navController.popBackStack()
                myGoalRegisterViewModel.consumeSideEffect()
                myGoalRegisterViewModel.resetUiState()
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
                MyGoalTopBar(navController)
            }

            // 운동 이미지
            item {
                Spacer(modifier = Modifier.height(80.dp))
                MyExerciseImage(
                    exerciseName = myGoalRegisterUiState.exerciseGoalName,
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                )
            }

            // 운동 선택 영역
            item {
                Spacer(modifier = Modifier.height(80.dp))
                MyExerciseInputText(
                    selectedExercise = myGoalRegisterUiState.exerciseGoalName,
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
                        myGoalRegisterViewModel.updateExerciseGoalValue(newValue)
                    },
                    value = inputValue
                )
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        MyButton(
            text = "추가하기",
            onClick = {
                myGoalRegisterViewModel.insertMyGoalInfo()
            },
            modifier = Modifier.padding(bottom = 24.dp)
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
            mode = 0,
            selectedExercise = selectedExercise,
            onItemSelected = { exercise ->
                selectedExercise = exercise
                myGoalRegisterViewModel.updateExerciseGoalName(exercise)
                showBottomSheet = false
            },
            onDismiss = { showBottomSheet = false },
            sheetState = sheetState
        )
    }
}
