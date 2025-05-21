package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.ssafy.fitmily_android.model.dto.response.family.FamilyDailyMember
import com.ssafy.fitmily_android.presentation.ui.main.family.FamilyScreen
import com.ssafy.fitmily_android.presentation.ui.main.family.detail.FamilyDetailScreen
import com.ssafy.fitmily_android.presentation.ui.main.family.exercise.FamilyExerciseScreen
import kotlinx.datetime.LocalDate
import okhttp3.internal.notify

fun NavGraphBuilder.familyNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "family/main"
        , route = BottomNavItem.Family.route
    ) {
        composable("family/main") {
            FamilyScreen(navController)
        }

        composable(
            route = "family/detail/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val dateArg = backStackEntry.arguments?.getString("date") ?: java.time.LocalDate.now().toString()
            FamilyDetailScreen(navController, dateArg)
        }

        composable(
            route = "family/exercise/{date}/{member}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val memberJson = backStackEntry.arguments?.getString("member")
            val member = Gson().fromJson(memberJson, FamilyDailyMember::class.java)
            FamilyExerciseScreen(
                navController = navController,
                date = date,
                member = member
            )
        }
    }
}