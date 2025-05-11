package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun ChatTopBar (
    navController: NavHostController
    , familyName: String = ""
    , familyCount: Int = -1
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .background(mainWhite)
            .padding(vertical = 10.dp)
    ) {
        IconButton(
            onClick = {
                navController.navigate("home/main") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            , modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 28.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.home_selected_icon),
                contentDescription = "뒤로가기"
            )
        }

        if (familyCount != -1) {
            Row(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = familyName, style = Typography.titleLarge, color = mainBlack
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text(
                    text = familyCount.toString(), style = Typography.titleLarge, color = mainBlack
                )
            }
        }
    }
}