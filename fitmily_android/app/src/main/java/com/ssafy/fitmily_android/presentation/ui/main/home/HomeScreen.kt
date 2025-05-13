package com.ssafy.fitmily_android.presentation.ui.main.home


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.presentation.ui.main.home.component.AloneHome
import com.ssafy.fitmily_android.presentation.ui.main.home.component.FamilyHome

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    var isInFamily = remember { mutableStateOf(false) }

    if (isInFamily.value) {
        FamilyHome(navController)
    } else {
        AloneHome()
    }

}






@Composable
@Preview(showSystemUi = true)
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}