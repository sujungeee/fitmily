package com.ssafy.fitmily_android.presentation.ui.main.family.exercise.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyExercise
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ExerciseUtil

@Composable
fun FamilyExerciseItem(
    familyDailyExercise: FamilyDailyExercise,
    modifier: Modifier
) {

    Log.d("test1234", "FamilyExerciseItem : familyDailyExercise : $familyDailyExercise")

    val isWalk = familyDailyExercise.exerciseName == "산책"
    val localImageRes = ExerciseUtil().mapExerciseNameToImage(familyDailyExercise.exerciseName) ?: R.drawable.sample_walk


    val unit = if(isWalk) {
        "km"
    }
    else {
        "회"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = mainWhite, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                if (isWalk) {
                    // exerciseRouteImg 사용 (URL 이미지)
                    AsyncImage(
                        model = familyDailyExercise.exerciseRouteImg,
                        contentDescription = "운동 루트 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(id = R.drawable.sample_walk),
                        error = painterResource(id = R.drawable.sample_walk)
                    )
                } else {
                    // 로컬 리소스 이미지 사용
                    Image(
                        painter = painterResource(id = localImageRes),
                        contentDescription = "운동 이미지",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column {
                // 운동 이름
                Text(
                    text = familyDailyExercise.exerciseName,
                    color = mainBlack,
                    style = Typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                // 횟수 or 거리, 칼로리, 운동시간 타이틀
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // 횟수 or 거리
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isWalk)
                                "거리"
                            else
                                "횟수",
                            color = mainBlack,
                            style = Typography.bodySmall
                        )
                    }

                    // 칼로리
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "칼로리",
                            color = mainBlack,
                            style = Typography.bodySmall
                        )
                    }

                    // 운동시간
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "운동시간",
                            color = mainBlack,
                            style = Typography.bodySmall
                        )
                    }
                }

                // 횟수 or 거리, 칼로리, 운동시간 값
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // 횟수 or 거리
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isWalk)
                                "${familyDailyExercise.exerciseCount} / ${familyDailyExercise.exerciseGoalValue} $unit"
                            else
                                "${familyDailyExercise.exerciseCount.toInt()} / ${familyDailyExercise.exerciseGoalValue} $unit",
                            color = mainBlack,
                            style = Typography.bodySmall
                        )
                    }

                    // 칼로리
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${familyDailyExercise.exerciseCalories} kcal",
                            color = mainBlack,
                            style = Typography.bodySmall
                        )
                    }

                    // 운동시간
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${familyDailyExercise.exerciseTime} 분",
                            color = mainBlack,
                            style = Typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}