package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.model.dto.response.home.GoalDto
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainGray

@Composable
fun GoalItem(item: GoalDto, userColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(16.dp)
                .background(mainGray, shape = RoundedCornerShape(100.dp))
        ){
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .background(mainBlue, shape = RoundedCornerShape(100.dp)),
                contentDescription = null,
                tint = Color.White,
                imageVector = androidx.compose.material.icons.Icons.Default.Check
            )
        }
        val last = if(item.exerciseGoalName=="산책") "km" else "회"
        Text(
            text = "${item.exerciseGoalValue}  ${item.exerciseGoalValue}${last}",
            style = typography.bodySmall,
            maxLines = 1,
        )
    }
}