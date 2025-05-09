package com.ssafy.fitmily_android.util

import com.ssafy.fitmily_android.R
import androidx.core.graphics.toColorInt

class Profile {
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

    // 활용: Color(strToColorInt("#FFD074BE"))
    fun strToColorInt(stringColor: String): Int {
        return stringColor.toColorInt()
    }
}