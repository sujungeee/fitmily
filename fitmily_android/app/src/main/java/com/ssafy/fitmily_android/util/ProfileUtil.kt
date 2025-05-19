package com.ssafy.fitmily_android.util

import androidx.compose.ui.graphics.Color
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.familyFifth
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.familyFourth
import com.ssafy.fitmily_android.ui.theme.familySecond
import com.ssafy.fitmily_android.ui.theme.familySixth
import com.ssafy.fitmily_android.ui.theme.familyThird

class ProfileUtil {
    // 활용: painterResource(typeToProfile("horse"))
    fun typeToProfile(iconType: String): Int? {
        val map = mapOf(
            "Mouse" to R.drawable.mouse_icon
            , "Cow" to R.drawable.cow_icon
            , "Tiger" to R.drawable.tiger_icon
            , "Rabbit" to R.drawable.rabbit_icon
            , "Dragon" to R.drawable.dragon_icon
            , "Snake" to R.drawable.snake_icon
            , "Horse" to R.drawable.horse_icon
            , "Sheep" to R.drawable.sheep_icon
            , "Monkey" to R.drawable.monkey_icon
            , "Chicken" to R.drawable.chicken_icon
            , "Dog" to R.drawable.dog_icon
            , "Pig" to R.drawable.pig_icon
        )

        return map[iconType]
    }

    // 가족 순서에 따른 color
    fun seqToColor(seq: Int): Color? {
        when (seq) {
            1 -> return familyFirst
            2 -> return familySecond
            3 -> return familyThird
            4 -> return familyFourth
            5 -> return familyFifth
            6 -> return familySixth
            else -> return null
        }
    }
}