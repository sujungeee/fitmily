package com.ssafy.fitmily_android.presentation.ui.main.my

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun MyExerciseHistoryItem(
    history: ExerciseHistory,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(mainWhite, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.health),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = history.time,
                    color = mainBlack,
                    style = Typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))

                if(history.unit == "km")
                    Text(
                        text = "더보기",
                        color = mainGray,
                        style = Typography.bodyMedium
                    )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text =
                        if(history.unit == "km")
                            "${history.exerciseName} ${history.exerciseCount} ${history.unit}"
                        else
                            "${history.exerciseName} ${history.exerciseCount.toInt()} ${history.unit}",
                    color = mainBlack,
                    style = Typography.bodyMedium
                )
            }
        }
    }
}