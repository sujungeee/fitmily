package com.ssafy.fitmily_android.presentation.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.presentation.navigation.bottom.BottomNavItem
import com.ssafy.fitmily_android.presentation.navigation.bottom.BottomNavigation
import com.ssafy.fitmily_android.presentation.navigation.bottom.chatNavGraph
import com.ssafy.fitmily_android.presentation.navigation.bottom.familyNavGraph
import com.ssafy.fitmily_android.presentation.navigation.bottom.homeNavGraph
import com.ssafy.fitmily_android.presentation.navigation.bottom.myNavGraph
import com.ssafy.fitmily_android.presentation.navigation.bottom.walkNavGraph

private const val TAG = "MainScreen_fitmily"
@Composable
fun MainScreen(parentNavController: NavHostController, fcmType: String?, fcmId: String?) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    LaunchedEffect(Unit) {
        val destination = when(fcmType) {
            "POKE" -> "home/main"
            "CHALLENGE" -> "home/main"
            "CHAT" -> "chat/main"
            "WALK_START" -> "walk/main"
            else -> "home/main"
        }
        navController.navigate(destination) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    val isBottomVisible = remember { mutableStateOf(true) }
    currentRoute?.let { route ->
        isBottomVisible.value = when (route) {
            "home/main", "family/main", "walk/main", "my/main" -> true
            else -> false
        }
    }

    Scaffold(
        bottomBar = {
            if (isBottomVisible.value) {
                BottomNavigation(
                    currentTab = BottomNavItem.items.find { currentRoute?.startsWith(it.route) == true }
                        ?: BottomNavItem.Home,
                    onTabSelected = { selectedTab ->
                        if (currentRoute?.startsWith(selectedTab.route) == false) {
                            navController.navigate("${selectedTab.route}/main") {
                                popUpTo(navController.graph.id) { // 백스택의 첫 번째 목적지: BottomNavItem.Home
                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            homeNavGraph(navController)
            familyNavGraph(navController)
            walkNavGraph(navController)
            chatNavGraph(navController)
            myNavGraph(parentNavController, navController)
        }
    }
}