package com.ssafy.fitmily_android.presentation.ui.main.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.home.profile.component.FamilyProfileItem
import com.ssafy.fitmily_android.presentation.ui.main.home.profile.component.FamilyProfileViewModel

@Composable
fun FamilyProfileScreen(navController: NavHostController, familyId:Int,
                        familyProfileViewModel: FamilyProfileViewModel = hiltViewModel()
) {
    val familyProfileUiState by familyProfileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        familyProfileViewModel.getFamilyHealth(familyId)
    }

    Column(
        modifier = Modifier
            .padding(
                top = 32.dp,
                bottom = 24.dp,
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 28.dp)
            ,horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    },
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "back",
            )
            Text(
                modifier = Modifier.padding(horizontal = 28.dp),
                text = "가족 건강 프로필",
                style = typography.headlineMedium,
            )

            Spacer(modifier = Modifier.size(10.dp))
        }
        LazyColumn(Modifier.padding(top = 32.dp, start = 28.dp, end = 28.dp)) {
            items(familyProfileUiState.familyHealthListData.familyHealthDto.size) { index ->
                FamilyProfileItem(familyProfileUiState.familyHealthListData.familyHealthDto[index])
            }
        }
    }
}








@Composable
@Preview(showSystemUi = true)
fun FamilyProfileScreenPreview() {
    FamilyProfileScreen(rememberNavController(),1)
}