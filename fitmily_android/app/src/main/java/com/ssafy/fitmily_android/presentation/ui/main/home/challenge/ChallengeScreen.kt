package com.ssafy.fitmily_android.presentation.ui.main.home.challenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.ui.theme.thirdBlue

@Composable
fun ChallengeScreen() {
    Column(
        modifier = Modifier
            .padding(
                top = 32.dp,
                bottom = 24.dp,
                start = 28.dp,
                end = 28.dp
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth()
            ,horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.myselectedicon),
                contentDescription = "logo",
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
                FamilyProfileItem(index = index + 1)
            }
        }
    }
}

@Composable
fun FamilyProfileItem(index: Int = 1) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp)),
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$index 등",
                style = typography.bodyMedium,
                color = mainDarkGray
            )
            Spacer(modifier = Modifier.size(8.dp))
            ProfileItem()

        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),

        ) {
            Column(
                modifier = Modifier
                    .background(mainWhite, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)  {
                        Text("총 거리", style = typography.bodyMedium, color = Color.Gray)
                        Text("3km", style = typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)  {
                        Text("총 산책시간", style = typography.bodyMedium, color = Color.Gray)
                        Text("30분", style = typography.bodyLarge)
                    }
                }
            }
        }
    }
}



@Composable
@Preview(showSystemUi = true)
fun ChallengeScreenPreview() {
    ChallengeScreen()
}