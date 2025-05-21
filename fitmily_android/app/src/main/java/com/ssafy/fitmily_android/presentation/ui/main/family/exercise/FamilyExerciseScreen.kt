package com.ssafy.fitmily_android.presentation.ui.main.family.exercise

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyExercise
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyMember
import com.ssafy.fitmily_android.presentation.ui.main.family.detail.FamilyDetailViewModel
import com.ssafy.fitmily_android.presentation.ui.main.family.exercise.component.FamilyExerciseItem
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.FamilyExerciseTopBar
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlack
import java.sql.Time

@Composable
fun FamilyExerciseScreen(
    navController: NavHostController,
    date: String,
    member: FamilyDailyMember
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray)
    ) {

        // Topbar 영역
        item {
            FamilyExerciseTopBar(
                navController = navController,
                text = member.userNickname
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

        items(member.exercises) { exercise ->
            FamilyExerciseItem(
                exercise,
                modifier = Modifier.padding(horizontal = 28.dp)
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}