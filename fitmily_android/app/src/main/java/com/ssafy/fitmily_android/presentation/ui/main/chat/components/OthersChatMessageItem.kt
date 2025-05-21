package com.ssafy.fitmily_android.presentation.ui.main.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun OthersChatMessageItem(
    othersMessage : ChatMessage
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(othersMessage.profileColor)
                    .size(width = 44.dp, height = 44.dp)
            ) {
                Image(
                    painter = painterResource(ProfileUtil().typeToProfile(othersMessage.profileIcon)!!)
                    , contentDescription = "typeImage"
                    , modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .size(width = 44.dp, height = 44.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column() {
                Text(
                    text = othersMessage.nickname
                    , style = Typography.bodyMedium
                    , color = mainBlack
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (othersMessage.message != null && othersMessage.message != "") { // 채팅 메시지
                        Box(
                            modifier = Modifier
                                .weight(1f, false)
                                .widthIn(max = 200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(mainWhite)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = othersMessage.message
                                , style = Typography.bodyMedium
                                , color = mainBlack
                            )
                        }
                    } else { // 채팅 이미지
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .fillMaxWidth(0.5f)
                                .aspectRatio(1f)
                        ) {
                            AsyncImage(
                                model = othersMessage.imageUrl
                                , contentDescription = "갤러리 이미지"
                                , contentScale = ContentScale.Crop
                                , alignment = Alignment.Center
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    ChatInfoItem("others", othersMessage.unReadCount, othersMessage.date)
                }
            }
        }
    }
}