package com.ssafy.fitmily_android.util

import com.ssafy.fitmily_android.R

class ExerciseUtil {

    fun mapExerciseNameToImage(name: String): Int? {
        val exerciseMap = mapOf(
            "런지" to R.drawable.lunge,
            "벤치프레스" to R.drawable.bench_press,
            "푸쉬업" to R.drawable.push_up,
            "스쿼트" to R.drawable.squat,
            "버피테스트" to R.drawable.burpee_test,
            "데드리프트" to R.drawable.dead_lift,
            "풀업" to R.drawable.pull_up,
            "딥스" to R.drawable.dips,
            "사이드레터럴레이즈" to R.drawable.side_lateral_raise
        )

        return exerciseMap[name]
    }
}