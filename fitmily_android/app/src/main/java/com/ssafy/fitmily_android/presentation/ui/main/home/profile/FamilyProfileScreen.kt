package com.ssafy.fitmily_android.presentation.ui.main.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.components.EmptyContentText
import com.ssafy.fitmily_android.presentation.ui.main.home.profile.component.FamilyProfileItem
import com.ssafy.fitmily_android.presentation.ui.main.home.profile.component.FamilyProfileViewModel

@Composable
fun FamilyProfileScreen(navController: NavHostController, familyId:Int,
                        familyProfileViewModel: FamilyProfileViewModel = hiltViewModel()
) {
    val familyProfileUiState by familyProfileViewModel.uiState.collectAsState()

    var isEmpty = remember { mutableStateOf(false) }
    LaunchedEffect(Unit){
        familyProfileViewModel.getFamilyHealth(familyId)
    }

    LaunchedEffect(familyProfileUiState.familyHealthListData) {
        if (familyProfileUiState.familyHealthListData.familyHealthDto.isEmpty()) {
            isEmpty.value = true
        }else{
            isEmpty.value = false
        }
    }

    Column(
        modifier = Modifier
            .padding(
                top = 32.dp,
                bottom = 24.dp,
            )
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
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
        if (isEmpty.value) {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                EmptyContentText(
                    title = "가족 건강 프로필이 없어요",
                    content = "건강 프로필을 등록해보세요",
                )
            }
            
        }else {
            LazyColumn(Modifier.padding(top = 32.dp, start = 28.dp, end = 28.dp)) {
                items(familyProfileUiState.familyHealthListData.familyHealthDto.size) { index ->
                    FamilyProfileItem(familyProfileUiState.familyHealthListData.familyHealthDto[index])
                }
            }
        }
    }
}








@Composable
@Preview(showSystemUi = true)
fun FamilyProfileScreenPreview() {
    FamilyProfileScreen(rememberNavController(),1)
}