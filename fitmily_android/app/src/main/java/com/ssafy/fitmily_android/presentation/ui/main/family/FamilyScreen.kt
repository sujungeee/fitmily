package com.ssafy.fitmily_android.presentation.ui.main.family

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.sample.compose.FamilyCalendar
import com.ssafy.fitmily_android.MainApplication
import com.ssafy.fitmily_android.presentation.ui.main.family.component.FamilyNameDotFlowRow
import com.ssafy.fitmily_android.ui.theme.familyFifth
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.familyFourth
import com.ssafy.fitmily_android.ui.theme.familySecond
import com.ssafy.fitmily_android.ui.theme.familySixth
import com.ssafy.fitmily_android.ui.theme.familyThird
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


    LaunchedEffect(Unit) {
        familyId = authDataStore.getFamilyId()
        familyViewModel.getFamilyCalendarInfo(
            familyId = 1, /* TODO 이거 바꿔야 함 임시로 넣어놓음 */
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
