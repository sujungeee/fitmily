package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.walk.component.StopWalkDialog
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun AloneHome(
    onClickJoin: (String) -> Unit,
    onClickCreate: (String) -> Unit,
) {
    val dialogState = remember { mutableStateOf(DialogState.NONE) }

    Column(
        modifier = Modifier
            .padding(
                top = 32.dp, start = 28.dp,
                end = 28.dp
            )
    ) {
        Text(
            text = "Fitmily",
            style = typography.titleLarge,
            color = mainBlue
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = "아직 등록된 가족이 없어요!\n" +
                    "가족을 등록하고, 운동 기록을 공유해보세요.",
            style = typography.titleMedium,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(mainWhite, shape = RoundedCornerShape(16.dp))
                    .clickable { dialogState.value = DialogState.JOIN }
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .weight(0.6f)
                        .aspectRatio(1f),
                    painter = painterResource(id = R.drawable.heart_icon),
                    contentDescription = "",
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "참여 코드로 가족 참여하기", style = typography.bodyMedium
                    )
                    Text(
                        text = "참여 코드 등록", style = typography.titleLarge, color = mainBlue
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { dialogState.value = DialogState.CREATE }
                    .background(mainWhite, shape = RoundedCornerShape(16.dp))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                ) {
                    Text(
                        text = "가족을 만들어 구성원 초대하기", style = typography.bodyMedium
                    )
                    Text(
                        text = "가족 생성하기", style = typography.titleLarge, color = mainBlue
                    )
                }
                Image(
                    modifier = Modifier
                        .weight(0.6f)
                        .aspectRatio(1f),
                    painter = painterResource(id = R.drawable.plus_icon),
                    contentDescription = "",
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )
        }
    }
    if (dialogState.value != DialogState.NONE) {
        FamilyDialog(
            onDismissRequest = { dialogState.value = DialogState.NONE },
            onConfirmation = {
                if(dialogState.value == DialogState.JOIN) {
                    onClickJoin(it)
                } else {
                    onClickCreate(it)
                }
                dialogState.value = DialogState.NONE

            },
            dialogState = dialogState.value

        )
    }
}


enum class DialogState {
    NONE,
    JOIN,
    CREATE
}
