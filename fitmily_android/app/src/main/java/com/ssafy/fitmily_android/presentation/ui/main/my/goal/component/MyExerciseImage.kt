package com.ssafy.fitmily_android.presentation.ui.main.my.goal.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R

@Composable
fun MyExerciseImage(
    exerciseName: String,
    modifier: Modifier
) {

    val exerciseMap = mapOf(
        "런지" to R.drawable.lunge,
        "벤치프레스" to R.drawable.bench_press,
        "푸쉬업" to R.drawable.push_up,
        "스쿼트" to R.drawable.squat,
        "버피테스트" to R.drawable.burpee_test,
        "데드리프트" to R.drawable.dead_lift,
        "풀업" to R.drawable.pull_up,
        "딥스" to R.drawable.dips,
        "사이드 레터럴 라이즈" to R.drawable.side_lateral_raise
    )

    val imageResId = exerciseMap[exerciseName] ?: R.drawable.sample_walk

    Box(
        modifier = modifier
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