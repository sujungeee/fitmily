package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyChatMessageItem(
    myMessage : ChatMessage
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        , contentAlignment = Alignment.CenterEnd
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(0.8f)
            , horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier.align(Alignment.Bottom)
            ) {
                ChatInfoItem("my", myMessage.unReadCount, myMessage.date)
            }

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            if (myMessage.message != null) { // 채팅 메시지
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(mainBlue)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = myMessage.message
                        , style = Typography.bodyMedium
                        , color = mainWhite
                    )
                }
            } else { // 채팅 이미지
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                ) {
                    Image(
                        // TODO: 채팅 이미지 삽입
                        painter = painterResource(R.drawable.tmp_image2)
                        , contentDescription = "갤러리 이미지"
                        , contentScale = ContentScale.Crop
                        , alignment = Alignment.Center
                    )
                }
            }
        }
    }
}