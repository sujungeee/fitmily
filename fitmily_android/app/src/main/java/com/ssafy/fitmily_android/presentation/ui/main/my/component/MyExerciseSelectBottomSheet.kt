package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyExerciseSelectBottomSheet(
    mode: Int,
    selectedExercise: String?,
    onItemSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {

    val exerciseList = if(mode == 0) listOf(
        "런지", "벤치프레스", "푸쉬업", "스쿼트", "버피테스트", "데드리프트", "풀업", "딥스", "사이드레터럴라이즈", "산책"
    )
    else listOf(
        "런지", "벤치프레스", "푸쉬업", "스쿼트", "버피테스트", "데드리프트", "풀업", "딥스", "사이드레터럴라이즈"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = mainWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "운동",
                style = Typography.titleLarge,
                color = mainBlack,
            )
            exerciseList.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemSelected(item)
                            onDismiss()
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        color = if (selectedExercise == item) mainBlack
                                else mainDarkGray,
                        style = Typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    if (selectedExercise == item) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_check),
                            contentDescription = "선택됨",
                            tint = mainBlack
                        )
                    }
                }
            }
        }
    }
}