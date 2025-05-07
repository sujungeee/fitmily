package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.walk.history.WalkHistoryScreen
import com.ssafy.fitmily_android.presentation.ui.main.walk.WalkScreen
import com.ssafy.fitmily_android.presentation.ui.main.walk.history.WalkHistoryDetailScreen

fun NavGraphBuilder.walkNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "walk/main"
        , route = BottomNavItem.Walk.route
    ) {
        composable("walk/main") {
            WalkScreen(navController)
        }

        composable("walk/history") {
            WalkHistoryScreen(navController)
        }
        composable("walk/detail") {
            WalkHistoryDetailScreen(navController)
        }
    }
}