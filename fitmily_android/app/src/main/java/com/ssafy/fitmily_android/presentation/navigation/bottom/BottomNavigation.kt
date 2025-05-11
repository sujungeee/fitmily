package com.ssafy.fitmily_android.presentation.navigation.bottom

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.navigation.bottom.BottomNavItem.Companion.items
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun BottomNavigation(
    currentTab: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit
) {
    NavigationBar (
        containerColor = mainWhite
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentTab == item
                , onClick = { onTabSelected(item) }
                , icon = {
                    val icon = if (currentTab == item) item.selectedIcon else item.unselectedIcon
                    androidx.compose.material3.Icon(
                        painter = painterResource(id = icon),
                        contentDescription = item.label
                    )
                }
                , alwaysShowLabel = false
                , colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

sealed class BottomNavItem(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val route: String
) {
    object Home : BottomNavItem("Home", R.drawable.home_selected_icon, R.drawable.home_unselected_icon, "home")
    object Family : BottomNavItem("Family", R.drawable.family_selected_icon, R.drawable.family_unselected_icon, "family")
    object Walk : BottomNavItem("Walk", R.drawable.walk_selected_icon, R.drawable.walk_unselected_icon, "walk")
    object Chat : BottomNavItem("Chat", R.drawable.chat_selected_icon, R.drawable.chat_unselected_icon, "chat")
    object My : BottomNavItem("My", R.drawable.my_selected_icon, R.drawable.my_unselected_icon, "my")

    companion object {
        val items = listOf(Home, Family, Walk, Chat, My)
    }
}