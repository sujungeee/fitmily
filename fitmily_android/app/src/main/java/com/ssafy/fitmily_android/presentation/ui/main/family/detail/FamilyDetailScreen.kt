package com.ssafy.fitmily_android.presentation.ui.main.family.detail

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.kizitonwose.calendar.sample.compose.FamilyWeekCalendar
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.presentation.ui.main.family.detail.component.FamilyDateBottomSheet
import com.ssafy.fitmily_android.presentation.ui.main.family.detail.component.FamilyStatsItem
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.FamilyDetailTopBar
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.familySecond
import com.ssafy.fitmily_android.ui.theme.familyThird
import com.ssafy.fitmily_android.ui.theme.mainBlack
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FamilyDetailScreen(
    navController: NavHostController,
    date: String,
    familyDetailViewModel: FamilyDetailViewModel = hiltViewModel()
) {

    val familyDetailUiState by familyDetailViewModel.familyDetailUiState.collectAsState()
    val authDataStore = MainApplication.getInstance().getDataStore()
    var familyId by remember { mutableStateOf(1) }
    var monthText by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.parse(date)) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateText = selectedDate.format(dateFormatter)

    LaunchedEffect(selectedDate) {
        familyId = authDataStore.getFamilyId()
        familyDetailViewModel.getFamilyDailyInfo(
            familyId = familyId,
            date = dateText
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Topbar 영역
        item {
            FamilyDetailTopBar(
                navController = navController,
                text = monthText,
                onClickBottomSheet = { showBottomSheet = true }
            )
        }

        // 주간 캘린더 영역
        item {
            FamilyWeekCalendar(
                selectedDate = selectedDate,
                onDateChange = { selectedDate = it },
                onWeekRangeChange = { month ->
                    monthText = month
                }
            )
        }

        // 날짜 영역
        item {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp)
            ) {
                Spacer(Modifier.height(32.dp))
                Text(
                    text = dateText,
                    style = Typography.titleLarge,
                    color = mainBlack
                )
                Spacer(Modifier.height(32.dp))
            }
        }

        // 해당 날짜별 가족 운동 대시보드
        items(familyDetailUiState.familyDailyResponse?.members ?: emptyList()) { stats ->
            Box(
                modifier = Modifier.padding(horizontal = 28.dp)
            ) {
                FamilyStatsItem(
                    stats = stats,
                    onClick = {
                        val memberJson = Uri.encode(Gson().toJson(stats))
                        navController.navigate("family/exercise/$dateText/$memberJson")
                    }
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }

    FamilyDateBottomSheet(
        visible = showBottomSheet,
        selectedDate = selectedDate,
        onDateChange = { selectedDate = it },
        onTodayClick = { selectedDate = LocalDate.now()},
        onConfirmClick = {
            monthText = "${selectedDate.year}.${selectedDate.monthValue}"
            showBottomSheet = false
        },
        onDismissClick = { showBottomSheet = false }
    )
}