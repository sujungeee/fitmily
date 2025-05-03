package com.ssafy.fitmily_android.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ssafy.fitmily_android.presentation.ui.auth.LoginScreen

fun NavGraphBuilder.authNavGraph() {
    navigation(
        startDestination = "login",
        route = RootNavGraph.AuthNavGraph.route // authNavGraph
    ) {
        composable("login") {
            LoginScreen()
        }
    }
}