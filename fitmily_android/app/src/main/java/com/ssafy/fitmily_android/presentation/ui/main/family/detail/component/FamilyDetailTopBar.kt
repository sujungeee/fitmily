package com.ssafy.fitmily_android.presentation.ui.main.my.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.ibmFontFamily
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun FamilyDetailTopBar (
    navController: NavHostController,
    text: String,
    onClickBottomSheet: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(mainWhite)
        ,
    ) {
        Row(
            modifier = Modifier
                .padding(top = 32.dp, bottom = 24.dp, start = 28.dp)
                .clickable { onClickBottomSheet() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = text,
                style = Typography.headlineLarge,
                color = mainBlack,
            )
            Icon(
                painter = painterResource(id = R.drawable.bottom_sheet_open),
                contentDescription = "날짜 바텀시트 띄우는 버튼",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}