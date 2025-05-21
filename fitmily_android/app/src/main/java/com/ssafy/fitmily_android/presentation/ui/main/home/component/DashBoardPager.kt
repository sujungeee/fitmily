package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.model.dto.response.home.FamilyDashboardDto
import com.ssafy.fitmily_android.model.dto.response.home.GoalDto
import com.ssafy.fitmily_android.presentation.ui.main.home.HomeViewModel
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun DashBoardPager(items: List<FamilyDashboardDto>,
                   onClickPoke : (Int) -> Unit,
) {
    if (items.isEmpty()) return


    val pagerState = rememberPagerState { items.size }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 40.dp),
        pageSpacing = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f),
            contentAlignment = Alignment.Center
        ) {
            DashBoardItem(item = items[page], onClickPoke = onClickPoke)
        }
    }
}




