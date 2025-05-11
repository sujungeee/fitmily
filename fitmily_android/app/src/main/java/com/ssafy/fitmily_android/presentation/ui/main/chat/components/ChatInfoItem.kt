package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.ssafy.fitmily_android.ui.theme.Typography
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
            , style = Typography.bodySmall
            , color = mainBlack
        )

        Text(
            text = DateUtil().getTime(date)
            , style = Typography.bodySmall
            , color = mainBlack
        )
    }
}