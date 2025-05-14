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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.main.my.GoalItem
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyButton
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalEditDialog
import com.ssafy.fitmily_android.presentation.ui.main.my.goal.component.MyGoalItem
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyGoalTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray

@Composable
fun MyGoalScreen(
    navController: NavHostController
) {

    // TODO 추후 UI State로 추출
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedGoal by remember { mutableStateOf<GoalItem?>(null) }

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

            // Topbar 영역
            item {
                MyGoalTopBar(navController)
                Spacer(Modifier.height(32.dp))
            }

            items(goals) { goal ->
                MyGoalItem(
                    goal = goal,
                    onEditClick = {
                        selectedGoal = goal
                        showEditDialog = true
                    },
                    onDeleteClick = { /* TODO 삭제 이벤트 */ }
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
                goalName = selectedGoal!!.name,
                initialValue = "${selectedGoal!!.total}",
                onDismiss = { showEditDialog = false },
                onConfirm = { newValue ->
                    /* TODO 실제 수정 로직 */
                    showEditDialog = false
                }
            )
        }
    }
}
