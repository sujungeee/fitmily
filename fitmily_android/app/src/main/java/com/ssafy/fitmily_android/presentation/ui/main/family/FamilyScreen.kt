package com.ssafy.fitmily_android.presentation.ui.main.family

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.sample.compose.FamilyCalendar
import com.ssafy.fitmily_android.presentation.ui.main.family.component.FamilyNameDotFlowRow
import com.ssafy.fitmily_android.ui.theme.familyFifth
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.familyFourth
import com.ssafy.fitmily_android.ui.theme.familySecond
import com.ssafy.fitmily_android.ui.theme.familySixth
import com.ssafy.fitmily_android.ui.theme.familyThird


@Composable
fun FamilyScreen(
    navController: NavHostController
) {

    val nameColorList = listOf(
        "예지렐라" to familyFirst,
        "수미동산" to familySecond,
        "수정아귀" to familyThird,
        "동옥변비" to familyFourth,
        "성현곤듀" to familyFifth,
        "용성예신" to familySixth
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            FamilyCalendar(
                modifier = Modifier.weight(1f),
                onDayClick = { day ->
                    navController.navigate("family/main/detail")
                }
            )
            FamilyNameDotFlowRow(
                nameColorList,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}