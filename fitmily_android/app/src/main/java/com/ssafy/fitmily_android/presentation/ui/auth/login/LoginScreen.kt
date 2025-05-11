package com.ssafy.fitmily_android.presentation.ui.auth.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.presentation.navigation.RootNavGraph
import com.ssafy.fitmily_android.presentation.ui.auth.components.ActivateButton
import com.ssafy.fitmily_android.presentation.ui.components.InputTextField
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavHostController
) {
    // TODO: uistate 분리
    var id by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
            , horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.fitmily_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(width = 200.dp, height = 75.dp)
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            ) {
                InputTextField(
                    modifier = Modifier.fillMaxWidth()
                    , "아이디"
                    , "아이디를 입력해주세요."
                    , "id"
                    , id
                    , { id = it }
                )
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                InputTextField(
                    modifier = Modifier.fillMaxWidth()
                    , "비밀번호"
                    , "비밀번호를 입력해주세요."
                    , "pwd"
                    , pwd
                    , { pwd = it}
                )
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            ActivateButton(
                onClick = {
                    // TODO: 로그인 성공시 main으로 navigate
                    navController.navigate("main") {
                        popUpTo(RootNavGraph.AuthNavGraph.route) {
                            inclusive = true
                        }
                    }
                }
                , text = "로그인"
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .clickable {
                    navController.navigate("join")
                }
            , verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fitmily"
                , color = mainBlue
                , style = Typography.titleLarge
            )

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Text(
                text = "회원가입"
                , color = mainBlue
                , style = Typography.bodyLarge
            )
        }
    }
}