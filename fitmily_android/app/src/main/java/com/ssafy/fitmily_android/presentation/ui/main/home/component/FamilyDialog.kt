package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite


@Composable
fun FamilyDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogState: DialogState = DialogState.NONE
) {
    var dialogTitle = ""
    var dialogContent = ""
    var confirmationText = "확인"

    when (dialogState) {
        DialogState.JOIN -> {
            dialogTitle = "초대받은 참여 코드를 작성하세요."
            dialogContent = "참여 코드를 입력해주세요!"
            confirmationText = "참여"
        }

        DialogState.CREATE -> {
            dialogTitle = "생성하고자 하는 가족명을 입력해주세요!"
            dialogContent = "2~8자 사이로 작성해주세요."
            confirmationText = "생성"
        }

        else -> ""
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = mainWhite),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = dialogTitle,
                    style = typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            dialogContent,
                            style = typography.bodyMedium,
                            color = mainDarkGray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .background(mainBlue, shape = RoundedCornerShape(100.dp)),
                    ) {
                        Text(
                            "취소", style = typography.bodyMedium,
                            color = mainWhite
                        )
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .background(mainGray, shape = RoundedCornerShape(100.dp)),
                    ) {
                        Text(
                            confirmationText, style = typography.bodyMedium,
                            color = mainBlack
                        )
                    }
                }
            }
        }
    }
}