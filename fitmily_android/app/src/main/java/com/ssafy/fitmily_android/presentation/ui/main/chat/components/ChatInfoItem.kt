package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.ibmFontFamily
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.util.DateUtil

@Composable
fun ChatInfoItem(
    type: String
    , unReadCount: Int
    , date: String
) {
    Column(
        horizontalAlignment = if (type == "my") Alignment.End else Alignment.Start
    ) {
        Text(
            text = unReadCount.toString()
            , style = TextStyle(
                fontFamily = ibmFontFamily
                , fontWeight = FontWeight.Medium
                , fontSize = 10.sp
            )
            , color = mainBlack
        )

        Text(
            text = DateUtil().getTime(date)
            , style = TextStyle(
                fontFamily = ibmFontFamily
                , fontWeight = FontWeight.Medium
                , fontSize = 10.sp
            )
            , color = mainBlack
        )
    }
}