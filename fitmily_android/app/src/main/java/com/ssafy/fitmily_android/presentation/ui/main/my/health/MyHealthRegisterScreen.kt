package com.ssafy.fitmily_android.presentation.ui.main.my.health

import EtcDiseaseChips
import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.ui.components.InputTextField
import com.ssafy.fitmily_android.presentation.ui.main.my.component.MyButton
import com.ssafy.fitmily_android.presentation.ui.main.my.health.component.Top5DiseaseChips
import com.ssafy.fitmily_android.presentation.ui.main.my.notification.component.MyHealthTopBar
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlack

@Composable
fun MyHealthRegisterScreen(
    navController: NavHostController
) {

    /* TODO 추후 UI STATE로 추출 예정 */
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val top5Diseases = listOf("암", "뇌혈관 질환", "심혈관 질환", "당뇨", "간 질환")
    var selectedTop5Disease by remember { mutableStateOf(setOf<String>()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {

            // TopBar 영역
            item {
                MyHealthTopBar(navController)
            }

            // 키 영역
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    InputTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = "키",
                        description = "키를 입력해주세요.",
                        inputType = "number",
                        value = height,
                        onValueChange = { height = it }
                    )
                }
            }

            // 몸무게 영역
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    InputTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = "몸무게",
                        description = "몸무게를 입력해주세요.",
                        inputType = "number",
                        value = weight,
                        onValueChange = { weight = it }
                    )
                }
            }

            // 5대 질환 영역
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "5대 질환",
                        style = typography.bodyLarge,
                        color = mainBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Top5DiseaseChips(
                        top5Diseases = top5Diseases,
                        selectedDiseases = selectedTop5Disease,
                        onDiseaseSelected = { disease ->
                            selectedTop5Disease = if (selectedTop5Disease.contains(disease)) {
                                selectedTop5Disease - disease // 이미 선택되어 있으면 해제
                            }
                            else {
                                selectedTop5Disease + disease // 아니면 추가
                            }
                        }
                    )
                }
            }

            // 기타 지병 영역
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "기타 지병",
                        style = typography.bodyLarge,
                        color = mainBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    EtcDiseaseChips()
                }
            }
        }

        MyButton(
            text = "건강 정보 등록하기",
            onClick = {
                /* TODO 건강 정보 등록하기 로직 */
                navController.popBackStack()
            },
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}
