package com.ssafy.fitmily_android.presentation.ui.main.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.my.MyExerciseDto
import com.ssafy.fitmily_android.presentation.ui.main.my.ExerciseHistory
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ExerciseUtil

@Composable
fun MyExerciseHistoryItem(
    history: MyExerciseDto
) {
    val isWalk = history.walkId != null

    val unit = if(isWalk) {
        "km"
    }
    else {
        "회"
    }

    val exerciseImageRes = ExerciseUtil().mapExerciseNameToImage(history.exerciseName) ?: R.drawable.sample_walk
    val imageUrl = history.imgUrl

    Column(
    ) {
        val valueText = if (isWalk) {
            history.exerciseRecord
        } else {
            history.exerciseRecord.toInt()
        }

        // 이미지
        if (isWalk && !imageUrl.isNullOrEmpty()) {
            // URL 이미지
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            // 리소스 이미지
            Image(
                painter = painterResource(id = exerciseImageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // 운동명
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = history.exerciseName,
                style = Typography.bodyLarge,
                color = mainBlack
            )
            if (history.exerciseName == "산책") {
                Text(
                    text = "더보기",
                    style = Typography.bodySmall,
                    color = mainDarkGray
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 칼로리, 거리 칩
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MyExerciseInfoChip(text = "${history.exerciseCalories}kcal")
            MyExerciseInfoChip(text = "${valueText} ${unit}")
        }
    }
}