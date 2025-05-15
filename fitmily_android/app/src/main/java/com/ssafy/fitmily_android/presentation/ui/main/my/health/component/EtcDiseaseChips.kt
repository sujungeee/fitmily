import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import com.ssafy.fitmily_android.ui.theme.mainDarkGray

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EtcDiseaseChips(
    chips: List<String>,
    onChipsChanged: (List<String>) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var inputFocused by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 기존 칩들
        chips.forEachIndexed { index, chipText ->
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = mainBlue,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (editingIndex == index) {
                        BasicTextField(
                            value = chipText,
                            onValueChange = { newValue ->
                                val newChips = chips.toMutableList().also { it[index] = newValue }
                                onChipsChanged(newChips)
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = mainBlack,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        )
                    } else {
                        Text(
                            text = chipText,
                            color = mainBlue,
                            style = Typography.bodyMedium,
                            modifier = Modifier
                                .clickable { editingIndex = index }
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.icon_delete),
                        contentDescription = "삭제",
                        tint = mainBlue,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                val newChips = chips.toMutableList().also { it.removeAt(index) }
                                onChipsChanged(newChips)
                                if (editingIndex == index) editingIndex = null
                            }
                    )
                }
            }
        }

        // 항상 마지막에 추가하기 칩
        Box(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = mainDarkGray, // mainGray
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    inputFocused = true
                    coroutineScope.launch {
                        delay(50)
                        focusRequester.requestFocus()
                    }
                }
        ) {
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = mainDarkGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        inputFocused = state.isFocused
                        // 포커스 잃었을 때 값이 있으면 추가
                        if (!state.isFocused && inputText.isNotBlank()) {
                            onChipsChanged(chips + inputText)
                            inputText = ""
                        }
                    },
                decorationBox = { innerTextField ->
                    if (inputText.isEmpty() && !inputFocused) {
                        Text(
                            text = "추가하기",
                            color = mainDarkGray, // mainGray
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (inputText.isNotBlank()) {
                            onChipsChanged(chips + inputText)
                            inputText = ""
                            focusManager.clearFocus()
                        }
                    }
                )
            )
        }
    }
}
