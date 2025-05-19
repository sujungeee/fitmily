package com.ssafy.fitmily_android.presentation.ui.main.my.goal.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import com.ssafy.fitmily_android.model.dto.response.my.MyGoalDto
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyGoalItem(
    goal: MyGoalDto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {

    val unit = when(goal.exerciseGoalName) {

        "산책" -> {
            "km"
        }

        else -> {
            "회"
        }
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = mainWhite,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = goal.exerciseGoalName,
                    style = Typography.bodyLarge,
                    color = mainBlack
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text =
                        if(unit == "km")
                            "${goal.exerciseGoalValue} $unit"
                        else
                            "${goal.exerciseGoalValue.toInt()} $unit",
                    style = Typography.bodyMedium,
                    color = mainBlack
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MyGoalItemActionButton(text = "수정", onClick = onEditClick)
                MyGoalItemActionButton(text = "삭제", onClick = onDeleteClick)
            }
        }
    }
}