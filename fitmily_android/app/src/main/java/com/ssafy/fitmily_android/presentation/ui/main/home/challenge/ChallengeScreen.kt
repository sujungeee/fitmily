package com.ssafy.fitmily_android.presentation.ui.main.home.challenge

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.home.challenge.component.ChallengeItem
import com.ssafy.fitmily_android.ui.theme.mainBlue

@Composable
fun ChallengeScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(
                top = 32.dp,
                bottom = 24.dp,
                start = 28.dp,
                end = 28.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                text = "산책 챌린지",
                style = typography.headlineMedium,
            )

            Spacer(modifier = Modifier.size(10.dp))
        }

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = "05.02 ~ 05.09",
            style = typography.bodyLarge,
            color = mainBlue
        )
        Text(
            text = "총 30km 걷기",
            style = typography.titleMedium,
        )

        LazyColumn(Modifier.padding(top = 32.dp)) {
            items(3) { index ->
                ChallengeItem(index = index + 1)
            }
        }
    }
}


@Composable
@Preview(showSystemUi = true)
fun ChallengeScreenPreview() {
    ChallengeScreen(rememberNavController())
}