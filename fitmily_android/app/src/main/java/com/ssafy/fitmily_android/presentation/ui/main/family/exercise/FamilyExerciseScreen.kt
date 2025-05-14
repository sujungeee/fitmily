package com.ssafy.fitmily_android.presentation.ui.main.family.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.family.exercise.component.FamilyExerciseItem
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.FamilyExerciseTopBar
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlack
import java.sql.Time

@Composable
fun FamilyExerciseScreen(
    navController: NavHostController
) {

    val date = "2025.04.25"

    val familyExercises = listOf(
        FamilyExercise(R.drawable.sample_walk, "런지", "개", 10f, 100, 3400, 20),
        FamilyExercise(R.drawable.sample_walk, "벤치프레스", "개", 10f, 100, 3400, 20),
        FamilyExercise(R.drawable.sample_walk, "푸시업", "개", 10f, 100, 3400, 20),
        FamilyExercise(R.drawable.sample_walk, "산책", "km", 10f, 100, 3400, 20),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray)
    ) {

        // Topbar 영역
        item {
            FamilyExerciseTopBar(
                navController = navController,
                text = "예지렐라"
            )
        }

        // 날짜
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 28.dp)
            ) {
                Text(
                    text = date,
                    color = mainBlack,
                    style = Typography.labelLarge
                )
            }
        }

        items(familyExercises) { exercise ->
            FamilyExerciseItem(
                exercise,
                modifier = Modifier.padding(horizontal = 28.dp)
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}


data class FamilyExercise(
    val exerciseImg: Int,
    val exerciseName: String,
    val exerciseUnit: String,
    val exerciseCurrent: Float,
    val exerciseTotal: Int,
    val exerciseKcal: Int,
    val exerciseTime: Int
)