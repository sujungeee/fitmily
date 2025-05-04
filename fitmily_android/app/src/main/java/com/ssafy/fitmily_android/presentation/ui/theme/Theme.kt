package com.ssafy.fitmily_android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = mainBlue,
    secondary = secondaryBlue,
    tertiary = thirdBlue
)

val LightColorScheme = lightColorScheme(
    primary = mainBlue,
    onPrimary = mainWhite,
    secondary = secondaryBlue,
    onSecondary = mainWhite,
    background = backGroundGray,
    onBackground = mainBlack,
    surface = mainWhite,
    onSurface = mainBlack,
    tertiary = thirdBlue,
    onTertiary = mainWhite,
)

@Composable
fun FitmilyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }


    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}