package com.ssafy.fitmily_android.presentation.ui.auth.login

import android.annotation.SuppressLint
import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
    , loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    val uiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.loginSideEffect) {
        for(sideEffect in uiState.loginSideEffect ?: return@LaunchedEffect) {
            when (sideEffect) {
                is LoginSideEffect.InitFCM -> {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@OnCompleteListener
                        }
                        val token = task.result
                        loginViewModel.sendFcmToken(token)
                    })
                }

                is LoginSideEffect.NavigateToMain -> {
                    navController.navigate("main") {
                        popUpTo(RootNavGraph.AuthNavGraph.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect (uiState.loginResult) {
        if (uiState.loginResult == "SUCCESS") {
            Toast.makeText(context, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        } else if (uiState.loginResult == "FAIL") {
            Toast.makeText(context, "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                , onClick = {
                    if (id.isNotEmpty() && pwd.isNotEmpty()) {
                        loginViewModel.login(id, pwd)
                    } else {
                        Toast.makeText(context, "빈 칸을 입력해주세요.", Toast.LENGTH_SHORT).show()
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