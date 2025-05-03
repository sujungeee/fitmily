package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.my.MyScreen

fun NavGraphBuilder.myNavGraph() {
    navigation(
        startDestination = "my/main"
        , route = BottomNavItem.My.route
    ) {
        composable("my/main") {
            MyScreen()
        }

        // TODO
        composable("my/datail") {
            MyScreen()
        }
    }
}