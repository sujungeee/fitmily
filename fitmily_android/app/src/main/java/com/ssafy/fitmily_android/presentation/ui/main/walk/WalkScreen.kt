package com.ssafy.fitmily_android.presentation.ui.main.walk

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun WalkScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(
                top = 32.dp,
                bottom = 24.dp,
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 28.dp),
                text = "산책",
                style = typography.headlineLarge,
            )
            Text(
                modifier = Modifier.padding(horizontal = 28.dp),
                text = "기록",
                style = typography.titleLarge,
                color = mainBlue
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 28.dp, end = 28.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("1km", style = typography.titleLarge)
                Text("거리", style = typography.bodyLarge, color = Color.Gray)
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(mainGray)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("10:00:22", style = typography.titleLarge)
                Text("시간", style = typography.bodyLarge, color = Color.Gray)
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(mainGray)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("90", style = typography.titleLarge)
                Text("심박수", style = typography.bodyLarge, color = Color.Gray)
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "walking",
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.End
            ) {
                items(5) { index ->
                    FilterChip(
                        onClick = { /*TODO*/ },
                        selected = false,
                        label = {
                            ProfileItem()
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = mainWhite,
                            labelColor = mainBlue,
                            selectedContainerColor = mainGray,
                        ),
                    )
                }

            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /*TODO*/ },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB)),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(72.dp) // 크기 조절
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.myselectedicon), // 정지 아이콘
                    contentDescription = "정지",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

        }


    }
}

@Composable
@Preview(showSystemUi = true)
fun WalkScreenPreview() {
    WalkScreen(rememberNavController())
}