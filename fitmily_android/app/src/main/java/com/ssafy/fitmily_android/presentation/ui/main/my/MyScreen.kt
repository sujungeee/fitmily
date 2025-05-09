package com.ssafy.fitmily_android.presentation.ui.main.my

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyAchievement
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyExerciseGoal
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyExerciseStatusGraph
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyRecordButtonRow
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyTobBar
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyTodayExerciseHistory
import com.ssafy.fitmily_android.ui.theme.backGroundGray

@Composable
fun MyScreen(
    navController : NavHostController
) {

    val goals = listOf(
        GoalItem("스쿼트", 69f, 100f, "회"),
        GoalItem("루마니안 데드리프트", 28f, 100f, "회"),
        GoalItem("산책", 0.65f, 1f, "km"),
    )

    val weekData = listOf(
        AchievementDay("일", 0.7f),
        AchievementDay("월", 0.9f),
        AchievementDay("화", 0.5f),
        AchievementDay("수", 0.8f),
        AchievementDay("목", 0.6f),
        AchievementDay("금", 1.0f),
        AchievementDay("토", 0.4f),
    )

    val histories = listOf(
        ExerciseHistory(R.drawable.health, "21시 33분", "런지", 10f, "회"),
        ExerciseHistory(R.drawable.health, "20시 33분", "산책", 1.2f, "km"),
        ExerciseHistory(R.drawable.health, "19시 33분", "런지", 10f, "회"),
        ExerciseHistory(R.drawable.health, "19시 33분", "런지", 10f, "회"),
        ExerciseHistory(R.drawable.health, "19시 33분", "런지", 10f, "회"),
    )

    Scaffold(
        topBar = {
            MyTobBar(
                profileImage = painterResource(id = R.drawable.my_unselected_icon),
                nickname = "예지렐라",
                // TODO 알림 화면으로 가기.
                onNotificationClick = { }
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = backGroundGray
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 28.dp)
        ) {

            /* 운동 현황 섹션 */
            // 운동 현황 원형 그래프
            item {
                Spacer(Modifier.height(32.dp))
                MyExerciseStatusGraph(progress = 80f)
            }

            // 운동 당일 목표
            item {
                Spacer(Modifier.height(12.dp))
                MyExerciseGoal(goals = goals)
            }

            // 건강 기록 + 목표 설정 + 운동 기록 버튼들
            item {
                Spacer(Modifier.height(12.dp))
                MyRecordButtonRow(
                    // TODO 건강 기록 화면으로 가기
                    onHealthClick = { },
                    // TODO 목표 설정 화면으로 가기
                    onGoalClick = { },
                    // TODO 운동 기록 화면으로 가기
                    onExerciseClick = { }
                )
            }

            /* 달성률 섹션 */
            item {
                Spacer(Modifier.height(32.dp))
                MyAchievement(data = weekData)
            }

            /* 오늘의 운동 히스토리 섹션 */
            item {
                Spacer(Modifier.height(32.dp))
                MyTodayExerciseHistory(
                    totalExerciseTime = "6시간 23분",
                    histories = histories
                )
            }
        }
    }
}

data class GoalItem(
    val name: String,
    val current: Float,
    val total: Float,
    val unit: String
)

data class AchievementDay(
    val day: String,
    val percent: Float
)

data class ExerciseHistory(
    val iconRes: Int,
    val time: String,
    val exerciseName: String,
    val exerciseCount: Float,
    val unit: String
)