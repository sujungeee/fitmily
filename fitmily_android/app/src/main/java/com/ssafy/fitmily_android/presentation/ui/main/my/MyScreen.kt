package com.ssafy.fitmily_android.presentation.ui.main.my

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.navigation.RootNavGraph
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyAchievement
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyExerciseGoal
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyExerciseStatusGraph
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyLogout
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyRecordButtonRow
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyTobBar
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyTodayExerciseHistory
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun MyScreen(
    parentNavController: NavHostController
    , navController : NavHostController
    , myViewMdodel: MyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by myViewMdodel.myUiState.collectAsStateWithLifecycle()
    val authDataStore = MainApplication.getInstance().getDataStore()
    var userNickname by remember { mutableStateOf("") }
    var userZodiacName by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        userNickname = authDataStore.getUserNickname()
        userZodiacName = authDataStore.getUserZodiacName()

        Log.d("test1234", "userNickname : $userNickname")
        Log.d("test1234", "userZodiacName : $userZodiacName")
        myViewMdodel.getMyGoalInfo()
        myViewMdodel.getMyExerciseInfo()
        myViewMdodel.getMyGoalWeeklyProgressInfo(authDataStore.getUserId())
    }

    LaunchedEffect(uiState.mySideEffect) {
        for(sideEffect in uiState.mySideEffect ?: return@LaunchedEffect) {
            when (sideEffect) {
                is MySideEffect.ClearAuthData -> {
                    authDataStore.clear()
                }

                is MySideEffect.NavigateToLogin -> {
                    parentNavController.navigate("login") {
                        popUpTo(RootNavGraph.MainNavGraph.route) {
                            inclusive = true
                        }
                    }
                    Toast.makeText(context, "로그아웃이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }

                is MySideEffect.NavigateToWalk -> {
                    /* TODO 산책 Detail로 가기 */
                }
            }
        }
    }

    val weekData = listOf(
        AchievementDay("5/10", 0.7f),
        AchievementDay("5/11", 0.9f),
        AchievementDay("5/12", 0.5f),
        AchievementDay("5/13", 0.8f),
        AchievementDay("5/14", 0.6f),
        AchievementDay("5/15", 1.0f),
        AchievementDay("5/16", 0.4f),
    )

    val histories = listOf(
        ExerciseHistory(R.drawable.sample_walk, "런지", 170, 12f, "회"),
        ExerciseHistory(R.drawable.sample_walk, "산책", 170, 1.5f, "km"),
        ExerciseHistory(R.drawable.sample_walk, "런지", 170, 12f, "회"),
        ExerciseHistory(R.drawable.sample_walk, "런지", 170, 12f, "회"),
        ExerciseHistory(R.drawable.sample_walk, "런지", 170, 12f, "회"),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray),
    ) {

        // TopBar 영역
        item {
            MyTobBar(
                userZodiacName = userZodiacName,
                color = ProfileUtil().seqToColor(0) ?: mainWhite,
                nickname = userNickname,
                onNotificationClick = {
                    navController.navigate("my/notification")
                }
            )
        }

        /* 운동 현황 섹션 */
        // 운동 현황 원형 그래프
        item {
            Spacer(Modifier.height(32.dp))
            MyExerciseStatusGraph(
                progress = uiState.myGoalInfo?.exerciseGoalProgress ?: 0,
                modifier = Modifier.padding(horizontal = 28.dp)
            )
        }

        // 운동 당일 목표
        item {
            Spacer(Modifier.height(12.dp))
            MyExerciseGoal(
                goals = uiState.myGoalInfo?.goal ?: emptyList(),
                modifier = Modifier.padding(horizontal = 28.dp)
            )
        }

        // 건강 기록 + 목표 설정 + 운동 기록 버튼들
        item {
            Spacer(Modifier.height(12.dp))
            MyRecordButtonRow(
                onHealthClick = {
                    navController.navigate("my/health")
                },
                onGoalClick = {
                    navController.navigate("my/goal")
                },
                onExerciseClick = {
                    navController.navigate("my/exercise")
                },
                modifier = Modifier.padding(horizontal = 28.dp)
            )
        }

        /* 달성률 섹션 */
        item {
            Spacer(Modifier.height(32.dp))
            MyAchievement(
                data = uiState.myGoalWeeklyProgressInfo?.goal ?: emptyList(),
                modifier = Modifier.padding(horizontal = 28.dp)
            )
        }

        /* 오늘의 운동 히스토리 섹션 */
        item {
            Spacer(Modifier.height(32.dp))
            MyTodayExerciseHistory(
                totalExerciseCalorie = uiState.myExerciseTotalCalorie,
                histories = uiState.myExerciseInfo?.exercise ?: emptyList(),
                modifier = Modifier.padding(horizontal = 28.dp)
            )
        }

        /* 로그아웃 섹션 */
        item {
            MyLogout {
                myViewMdodel.logout()
            }
        }
    }
}

data class AchievementDay(
    val day: String,
    val percent: Float
)

data class ExerciseHistory(
    val iconRes: Int,
    val exerciseName: String,
    val exerciseCalorie: Int,
    val exerciseValue: Float,
    val unit: String
)