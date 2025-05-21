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
import com.ssafy.fitmily_android.util.ExerciseUtil

@Composable
fun MyExerciseImage(
    exerciseName: String,
    modifier: Modifier
) {

    val imageResId = ExerciseUtil().mapExerciseNameToImage(exerciseName) ?: R.drawable.sample_walk

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