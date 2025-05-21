package com.ssafy.fitmily_android.presentation.ui.main.my.component

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.model.dto.response.my.MyWeaklyGoalDto
import com.ssafy.fitmily_android.presentation.ui.main.my.AchievementDay
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MyAchievement(
    data: List<MyWeaklyGoalDto>,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "달성률 (지난 7일)",
            color = mainBlack,
            style = Typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {

                if(data.size < 2) return@Canvas

                val space = size.width / (data.size - 1)
                val maxY = size.height * 0.8f
                val minY = size.height * 0.2f

                val points = data.mapIndexed { idx, day ->
                    Offset(
                        x = idx * space,
                        y = minY + (1f - (day.exerciseGoalProgress / 100)) * (maxY - minY)
                    )
                }

                // 선 그리기
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for(i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }
                drawPath(
                    path = path,
                    color = mainBlue,
                    style = Stroke(width = 2.dp.toPx())
                )

                // 점 찍기
                points.forEachIndexed { idx, offset ->
                    // 점
                    drawCircle(
                        color = mainBlue,
                        radius = 4.dp.toPx(),
                        center = offset
                    )
                    // 값
                    val percentText = "${(data[idx].exerciseGoalProgress)}%"
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            percentText,
                            offset.x,
                            offset.y - 10.dp.toPx(), // 점 위 10dp 위치
                            Paint().apply {
                                color = Color.parseColor("#141414")
                                textSize = 12.sp.toPx()
                                textAlign = Paint.Align.CENTER
                                isAntiAlias = true
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { day ->
                Text(
                    text = formatToMonthDay(day.date),
                    color = mainBlack,
                    style = Typography.bodyMedium
                )
            }
        }
    }
}

fun formatToMonthDay(dateStr: String): String {
    val parsed = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
    return "${parsed.monthValue}/${parsed.dayOfMonth}"
}