package com.ssafy.fitmily_android.presentation.ui.main.family

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.sample.compose.FamilyCalendar
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.presentation.ui.main.family.component.FamilyNameDotFlowRow
import java.time.LocalDate


@Composable
fun FamilyScreen(
    navController: NavHostController,
    familyViewModel: FamilyViewModel = hiltViewModel()
) {

    val familyUiState by familyViewModel.familyUiState.collectAsState()
    val authDataStore = MainApplication.getInstance().getDataStore()
    var familyId by remember { mutableStateOf(1) }
    var today = remember { LocalDate.now() }
    val calendarData = familyUiState.familyCalendarResponse?.calendar ?: emptyList()


    LaunchedEffect(Unit) {
        familyId = authDataStore.getFamilyId()
        Log.d("test1234", "FamilyScreen familyId: $familyId")
        familyViewModel.getFamilyCalendarInfo(
            familyId = 1, /* TODO familyId로 바꾸기 */
            year = today.year,
            month = today.monthValue.toString().padStart(2, '0')
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 월간 캘린더 영역
        FamilyCalendar(
            modifier = Modifier.weight(1f),
            indicatorMap = familyViewModel.buildIndicatorMap(calendarData),
            onDayClick = { day ->
                navController.navigate("family/detail")
            }
        )

        // 가족 정보 영역
        FamilyNameDotFlowRow(
            families = familyUiState.familyCalendarResponse?.members ?: emptyList(),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
