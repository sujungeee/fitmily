package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssafy.fitmily_android.presentation.ui.main.family.FamilyScreen

fun NavGraphBuilder.familyNavGraph() {
    navigation(
        startDestination = "family/main"
        , route = BottomNavItem.Family.route
    ) {
        composable("family/main") {
            FamilyScreen()
        }

        // TODO
        composable("family/detail") {
            FamilyScreen()
        }
    }
}