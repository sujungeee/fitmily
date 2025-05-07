package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun WeatherCard(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(mainWhite, shape = RoundedCornerShape(16.dp))
        .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,){

        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.myselectedicon),
            contentDescription = "item",
        )
        Spacer(Modifier.weight(1f))

        Text(
            text = "오늘은 강수 확률이 높아요.\n실내운동을 추천드려요",
            style = typography.bodyMedium
        )

        Spacer(Modifier.weight(1f))
        Column {
            Text(
                text = "현재 기온",
                style = typography.bodyMedium,
                color = mainDarkGray
            )

            Text(
                text = "20℃"
                , style = typography.titleLarge
            )
        }
        Spacer(Modifier.weight(1f))
    }
}