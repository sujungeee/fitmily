package com.ssafy.fitmily_android.presentation.ui.main.my.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.model.dto.response.my.MyGoalDto
import com.ssafy.fitmily_android.presentation.ui.main.my.GoalItem
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyButton
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalEditDialog
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalItem
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyGoalTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray

@Composable
fun MyGoalScreen(
    navController: NavHostController,
    myGoalViewModel: MyGoalViewModel = hiltViewModel()
) {

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf<MyGoalDto?>(null) }
    val myGoalUiState by myGoalViewModel.myGoalUiState.collectAsState()

    LaunchedEffect(Unit) {
        myGoalViewModel.getMyGoalInfo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            // Topbar 영역
            item {
                MyGoalTopBar(navController)
                Spacer(Modifier.height(32.dp))
            }

            items(myGoalUiState.myGoalInfo?.goal ?: emptyList()) { goal ->
                MyGoalItem(
                    goal = goal,
                    onEditClick = {
                        selectedGoal = goal
                        showEditDialog = true
                    },
                    onDeleteClick = { myGoalViewModel.deleteMyGoalInfo(selectedGoal!!.goalId) }
                )
            }
        }

        MyButton(
            text = "목표 추가",
            onClick = { navController.navigate("my/goal/register") },
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if(showEditDialog && selectedGoal != null) {
            MyGoalEditDialog(
                goalName = selectedGoal!!.exerciseGoalName,
                initialValue = "${selectedGoal!!.exerciseGoalValue}",
                onDismiss = { showEditDialog = false },
                onConfirm = { newValue ->
                    myGoalViewModel.patchMyGoalInfo(
                        goalId = selectedGoal!!.goalId,
                        exerciseGoalValue = 1f
                    )
                    showEditDialog = false
                }
            )
        }
    }
}
