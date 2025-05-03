package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.walk.WalkScreen

fun NavGraphBuilder.walkNavGraph() {
    navigation(
        startDestination = "walk/main"
        , route = BottomNavItem.Walk.route
    ) {
        composable("walk/main") {
            WalkScreen()
        }

        // TODO
        composable("walk/detail") {
            WalkScreen()
        }
    }
}