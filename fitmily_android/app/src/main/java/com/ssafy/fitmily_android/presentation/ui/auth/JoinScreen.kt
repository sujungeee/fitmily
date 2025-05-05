package com.ssafy.fitmily_android.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
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
import com.ssafy.fitmily_android.presentation.navigation.RootNavGraph
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun JoinScreen(
    navController: NavHostController
) {
    // TODO: uistate 분리
    var id by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var pwd2 by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("남") }
    var idState by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray)
    ) {
        Column (
            modifier = Modifier
                .padding(horizontal = 28.dp)
        ){
            Text(
                text = "Fitmily"
                , modifier = Modifier.fillMaxWidth()
                    .padding(top = 32.dp)
                , color = mainBlue
                , style = Typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                , horizontalArrangement = Arrangement.Start
                , verticalAlignment = Alignment.Bottom
            ) {
                InputTextField(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                    ,"아이디"
                    , "영어, 숫자 포함 최대 8자까지 가능합니다."
                    , "id"
                    , id
                    , { id =  it }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        // TODO: 중복 확인
                    }
                    , modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = "중복 확인"
                        , color = mainWhite
                        , style = Typography.bodyMedium
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            if (idState) {
                Text(
                    text = "아이디가 중복되었어요.",
                    color = mainBlack,
                    style = Typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            InputTextField(
                modifier = Modifier.fillMaxWidth()
            ,"비밀번호"
                , "영어, 숫자, 특수문자(!@#\$%) 포함 8~12자로 가능합니다."
                , "pwd"
                , pwd
                , { pwd =  it }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            InputTextField(
                modifier = Modifier.fillMaxWidth()
                , "비밀번호 확인"
                , "새 비밀번호를 다시 입력해 주세요."
                , "pwd"
                , pwd2
                , { pwd2 =  it }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            InputTextField(
                modifier = Modifier.fillMaxWidth()
                , "닉네임"
                , "영어, 숫자 포함 최대 8자까지 가능합니다."
                , "default"
                , nickname
                , { nickname =  it }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            InputTextField(
                modifier = Modifier.fillMaxWidth()
                , "생년월일"
                , "생년월일을 입력해주세요.(예:20000101)"
                , "number"
                , birth
                , { birth = it }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "성별"
                , style = typography.bodyLarge
                , color = mainBlack
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            GenderSelector(
                gender
                , onGenderSelected = {
                    gender = it
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
            , contentAlignment = Alignment.BottomCenter
        ) {
            ActivateButton(
                onClick = {
                    // TODO: 회원가입 성공시 login navigate
                    navController.navigate("login") {
                        popUpTo(RootNavGraph.AuthNavGraph.route) {
                            inclusive = false
                        }
                    }
                }
                , text = "회원가입"
            )
        }
    }
}