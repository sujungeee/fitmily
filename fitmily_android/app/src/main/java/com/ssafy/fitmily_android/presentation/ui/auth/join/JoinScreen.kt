package com.ssafy.fitmily_android.presentation.ui.auth.join

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.presentation.navigation.RootNavGraph
import com.ssafy.fitmily_android.presentation.ui.auth.components.ActivateButton
import com.ssafy.fitmily_android.presentation.ui.auth.join.components.GenderSelector
import com.ssafy.fitmily_android.presentation.ui.components.InputTextField
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

private const val TAG = "JoinScreen_fitmily"
@Composable
fun JoinScreen(
    navController: NavHostController
    , joinViewModel: JoinViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    var id by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var pwd2 by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("남") }
    var joinState by remember { mutableStateOf("Not Initialized") }

    val uiState by joinViewModel.joinUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isJoinAvailable) {
        if (uiState.isJoinAvailable == "Available") {
            joinState = "Available"
        } else if (uiState.isJoinAvailable == "Not Available") {
            joinState = "Not Available"
        }
    }

    LaunchedEffect(uiState.joinSideEffect) {
        when (uiState.joinSideEffect) {
            is JoinSideEffect.NavigateToLogin -> {
                Toast.makeText(context, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo(RootNavGraph.AuthNavGraph.route) {
                        inclusive = false
                    }
                }
            }
            null -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backGroundGray)
            .padding(horizontal = 28.dp)
    ) {
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            item {
                Text(
                    text = "Fitmily"
                    , modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                    , color = mainBlue
                    , style = Typography.titleLarge
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    InputTextField(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        "아이디",
                        "영어, 숫자 포함 4~8자까지 가능합니다.",
                        "id",
                        id,
                        { id = it },
                        enabled = joinState == "Not Initialized" || joinState == "Not Available"
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Button(
                        onClick = {
                            if (JoinUtil().isValidId(id)) {
                                joinViewModel.checkDuplId(id)
                            } else {
                                Toast.makeText(context, "아이디 형식이 맞지 않습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }, modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = "중복 확인", color = mainWhite, style = Typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
            }


            item {
                if (joinState == "Not Available") {
                    Text(
                        text = "아이디가 중복되었어요.",
                        color = mainBlack,
                        style = Typography.bodyMedium
                    )
                }
            }

            item {
                if (joinState == "Available") {
                    Text(
                        text = "사용 가능한 아이디입니다.",
                        color = mainBlack,
                        style = Typography.bodyMedium
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }

            item {
                InputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    "비밀번호",
                    "영어, 숫자, 특수문자(!@#\$%) 포함 8~12자까지 가능합니다.",
                    "pwd",
                    pwd,
                    { pwd = it }
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }

            item {
                InputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    "비밀번호 확인",
                    "새 비밀번호를 다시 입력해 주세요.",
                    "pwd",
                    pwd2,
                    { pwd2 = it }
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }

            item {
                InputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    "닉네임",
                    "한글 2~8자까지 가능합니다.",
                    "default",
                    nickname,
                    { nickname = it }
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }

            item {
                InputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    "생년월일",
                    "생년월일을 입력해주세요.(예:20000101)",
                    "number",
                    birth,
                    { birth = it }
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }

            item {
                Text(
                    text = "성별", style = typography.bodyLarge, color = mainBlack
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            item {
                GenderSelector(
                    gender, onGenderSelected = {
                        gender = it
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
            , verticalArrangement = Arrangement.Bottom
            , horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActivateButton(
                modifier = Modifier.fillMaxWidth()
                , onClick = {
                    // 회원가입
                    val isInputValid = JoinUtil().isInputValid(id, pwd, pwd2, nickname, birth)
                    val isValidId = JoinUtil().isValidId(id)
                    val isValidPwd = JoinUtil().isValidPwd(pwd)
                    val isValidNickname = JoinUtil().isValidNickname(nickname)
                    val isValidBirth = JoinUtil().isValidBirth(birth)
                    val isFormatValid = JoinUtil().isFormatValid(id, pwd, nickname, birth)
                    val isEqualsPwd = JoinUtil().isEqualsPwd(pwd, pwd2)
                    val joinStateValid = joinState == "Available"

                    if (isInputValid && isFormatValid && isEqualsPwd &&joinStateValid) {
                        joinViewModel.join(id, pwd, nickname, birth, if (gender.equals("남")) 0 else 1)
                    } else {
                        val message = when {
                            !isInputValid -> "빈 칸을 입력해주세요."
                            !isValidId -> "아이디 형식이 맞지 않습니다."
                            !isValidPwd -> "비밀번호 형식이 맞지 않습니다."
                            !isValidNickname -> "닉네임 형식이 맞지 않습니다."
                            !isValidBirth -> "생년월일 형식이 맞지 않습니다."
                            !isEqualsPwd -> "비밀번호가 일치하지 않습니다."
                            !joinStateValid -> "아이디 중복을 확인해주세요."
                            else -> ""
                        }
                        if (message.isNotEmpty()) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                , text = "회원가입"
            )
        }
    }
}