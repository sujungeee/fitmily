package com.ssafy.fitmily_android.presentation.ui.main.family.detail.component

import android.graphics.Paint.Align
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.commandiron.wheel_picker_compose.core.SelectorProperties
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.home.component.AloneHome
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.ui.theme.secondaryBlue
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyDateBottomSheet(
    visible: Boolean,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onTodayClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val desiredWidth = screenWidth - 56.dp


    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismissClick,
            dragHandle = null,
            containerColor = mainWhite
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 28.dp)
            ) {
                // 바텀시트 타이틀
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "날짜 선택",
                        style = Typography.bodyLarge
                    )
                    IconButton(
                        onClick = onDismissClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_delete),
                            contentDescription = "바텀시트 닫기",
                            tint = mainBlack
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                WheelDatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    startDate = selectedDate,
                    yearsRange = 2000..2030,
                    size = DpSize(desiredWidth, 200.dp),
                    rowCount = 5,
                    textStyle = Typography.bodyLarge,
                    textColor = mainBlack,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        shape = RoundedCornerShape(16.dp),
                        color = secondaryBlue,
                        border = BorderStroke(1.dp, mainBlue)
                    ),
                    onSnappedDate = { date ->
                        onDateChange(date)
                    }
                )

                Spacer(Modifier.height(32.dp))

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onTodayClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("오늘")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = onConfirmClick,
                        modifier = Modifier.weight(2f)
                    ) {
                        Text("선택한 날짜 이동")
                    }
                }
            }
        }
    }
}