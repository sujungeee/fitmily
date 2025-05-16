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
            "mouse" to R.drawable.mouse_icon
            , "cow" to R.drawable.cow_icon
            , "tiger" to R.drawable.tiger_icon
            , "rabbit" to R.drawable.rabbit_icon
            , "dragon" to R.drawable.dragon_icon
            , "snake" to R.drawable.snake_icon
            , "horse" to R.drawable.horse_icon
            , "sheep" to R.drawable.sheep_icon
            , "monkey" to R.drawable.monkey_icon
            , "chicken" to R.drawable.chicken_icon
            , "dog" to R.drawable.dog_icon
            , "pig" to R.drawable.pig_icon
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