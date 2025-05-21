package com.ssafy.fitmily_android.presentation.ui.main.home.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse
import com.ssafy.fitmily_android.presentation.ui.main.home.HomeUiState
import com.ssafy.fitmily_android.presentation.ui.main.home.HomeViewModel
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite
import kotlin.math.log

private const val TAG = "FamilyHome"
@Composable
fun FamilyHome(
    navController: NavHostController,
    homeUiState: HomeUiState,
    onClickPoke : (Int) -> Unit,
) {

    LaunchedEffect(homeUiState) {
        Log.d(TAG, "FamilyHome: $homeUiState")
    }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box(){
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(
                        top = 32.dp,
                    )
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    text = "Fitmily",
                    style = typography.headlineMedium,
                    color = mainBlue
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = homeUiState.family.familyName,
                            style = typography.headlineLarge,
                        )
                        Text(
                            text = "초대 코드 복사",
                            style = typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.clickable {
                                clipboardManager.setText(AnnotatedString(homeUiState.family.familyInviteCode))
                                Toast.makeText(
                                    context,
                                    "초대코드가 복사되었습니다: ${homeUiState.family.familyInviteCode}",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        )
                    }
                    Text(
                        text = "가족 건강 프로필", style = typography.bodySmall,
                        color = mainWhite,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .background(mainBlue, shape = ButtonDefaults.shape)
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("home/family/${homeUiState.familyId.toInt()}")
                            },
                    )

                }


                Column(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 28.dp),
                        text = "가족 건강 대시보드",
                        style = typography.titleLarge,
                    )
                    DashBoardPager(
                        homeUiState.dashBoardListData.familyDashboardDto,
                        onClickPoke = onClickPoke
                    )
                }
                if (homeUiState.challengeData.startDate.isNotBlank()) {
                    Column(
                        modifier = Modifier.padding(
                            top = 24.dp, start = 28.dp,
                            end = 28.dp
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = 12.dp),
                            text = "산책 챌린지",
                            style = typography.titleLarge,
                        )

                        ChallengeCard(navController, homeUiState.challengeData)
                    }
                }
                Column(
                    modifier = Modifier.padding(
                        top = 24.dp, start = 28.dp,
                        end = 28.dp
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = "오늘의 날씨",
                        style = typography.titleLarge,
                    )
                    WeatherCard(
                        weather = homeUiState.weather,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
        if (homeUiState.isLoading) {
            Log.d(TAG, "FamilyHome: 로딩중중주우ㅜㅜㅜㅜㅜ")
            Surface {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(mainWhite),
                    contentDescription = null,
                    painter = painterResource(R.drawable.plus_icon),
                )
            }
        }
    }
}