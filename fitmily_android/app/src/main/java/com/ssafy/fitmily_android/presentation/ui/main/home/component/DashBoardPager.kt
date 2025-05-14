package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun DashBoardPager(items: List<String>) {
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
            DashBoardItem(item = items[page])
        }
    }
}

@Composable
fun DashBoardItem(item: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(mainWhite, shape = RoundedCornerShape(16.dp))
            .padding(20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileItem()

            TextButton(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    mainWhite
                )
            ) {
                Text(
                    modifier = Modifier.background(mainBlue, shape = RoundedCornerShape(100.dp)).padding(4.dp),
                    text = "콕 찌르기",
                    style = typography.bodyMedium,
                    color = mainWhite,
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.55f)
                ) {
                    items(5) { index ->
                        GoalItem(index)
                    }
                }
                Spacer(Modifier.size(20.dp))

                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(8.dp)
                            .aspectRatio(1f)
                            .fillMaxSize(),
                        color = mainBlue,
                        strokeWidth = 8.dp,
                        progress = 0.8f
                    )

                    Text(
                        text = "80%",
                        style = typography.bodyMedium,
                    )

                }

            }
        }
    }
}


@Composable
fun GoalItem(index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(16.dp)
                .background(mainGray, shape = RoundedCornerShape(100.dp))
        ){
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .background(mainBlue, shape = RoundedCornerShape(100.dp)),
                contentDescription = null,
                tint = Color.White,
                imageVector = androidx.compose.material.icons.Icons.Default.Check
            )
        }
//        Checkbox(
//            modifier = Modifier.padding(6.dp).size(20.dp),
//            checked = true,
//            enabled = false,
//            onCheckedChange = { /*TODO*/ },
//            colors = CheckboxDefaults.colors(
//                disabledCheckedColor = mainBlue,
//                disabledUncheckedColor = mainGray
//            )
//        )
        Text(
            text = "${index}번째 목표 달성 완료",
            style = typography.bodySmall,
            maxLines = 1,
        )
    }
}