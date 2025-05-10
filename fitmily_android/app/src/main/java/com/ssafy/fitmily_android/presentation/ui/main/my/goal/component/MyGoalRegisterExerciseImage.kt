package com.ssafy.fitmily_android.presentation.ui.main.my.goal.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R

@Composable
fun MyGoalRegisterExerciseImage(
    exerciseName: String
) {

    val exerciseMap = mapOf(
        "런지" to R.drawable.sample_walk,
        "벤치프레스" to R.drawable.sample_walk,
        "푸시업" to R.drawable.sample_walk,
        "스쿼트" to R.drawable.sample_walk,
        "버피테스트" to R.drawable.sample_walk,
        "데드리프트" to R.drawable.sample_walk,
        "풀업" to R.drawable.sample_walk,
        "딥스" to R.drawable.sample_walk,
        "사이드 레터럴 라이즈" to R.drawable.sample_walk,
        "산책" to R.drawable.sample_walk
    )

    val imageResId = exerciseMap[exerciseName] ?: R.drawable.sample_walk

    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "운동 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}