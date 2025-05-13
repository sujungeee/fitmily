package com.kizitonwose.calendar.sample.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.FamilyDetailTopBar
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyWeekCalendar(
    close: () -> Unit = {},
    onWeekRangeChange: (String) -> Unit = {}
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )
    val start = state.firstVisibleWeek.days.first().date
    val end = state.firstVisibleWeek.days.last().date
    val currentMonth = start.format(DateTimeFormatter.ofPattern("yyyy.MM"))

    // 주간 범위 바뀔 때마다 호출
    LaunchedEffect(currentMonth) {
        onWeekRangeChange(currentMonth)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainWhite),
    ) {
        WeekCalendar(
            state = state,
            dayContent = { day ->
                Day(day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                    }
                }
            },
        )
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {

    val days = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = days,
                color = mainBlack,
                style = Typography.bodyMedium
            )
            Text(
                text = dateFormatter.format(date),
                color = if (isSelected) mainBlue else mainBlack,
                style = Typography.bodyMedium
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(mainBlue)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun NavigationIcon(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(shape = CircleShape)
            .clickable(role = Role.Button, onClick = onBackClick),
    ) {
        Icon(
            tint = Color.White,
            modifier = Modifier.align(Alignment.Center),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
        )
    }
}
