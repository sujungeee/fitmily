package com.ssafy.fitmily_android.presentation.ui.main.my.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun FamilyDetailTopBar (
    navController: NavHostController,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(mainWhite)
        ,
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()
            }
            , modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 28.dp, top = 10.dp, bottom = 10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "뒤로가기"
            )
        }

        Text(
            text = text,
            style = Typography.headlineMedium,
            color = mainBlack,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}