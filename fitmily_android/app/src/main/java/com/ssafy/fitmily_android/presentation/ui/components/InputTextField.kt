package com.ssafy.fitmily_android.presentation.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.DecorationBox
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.fitmily_android.ui.theme.ibmFontFamily
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainDarkGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    modifier: Modifier
    , title: String
    , description: String
    , inputType: String
    , value: String
    , onValueChange: (String) -> Unit
    , fontSize : Int = 16
    , enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val visualTransformation = when(inputType) {
        "pwd" -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    Column(modifier = modifier) {
        Text(
            text = title
            , style = typography.bodyLarge
            , color = mainBlack
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        BasicTextField(
            value = value
            , onValueChange = onValueChange
            , modifier = modifier
                .indicatorLine(
                    enabled = true
                    , isError = false
                    , interactionSource = interactionSource
                    , colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = mainDarkGray
                        , unfocusedIndicatorColor = mainDarkGray
                    )
                )
            , enabled = enabled
            , keyboardOptions = when (inputType) {
                "number" -> KeyboardOptions(keyboardType = KeyboardType.Number)
                "pwd" -> KeyboardOptions(keyboardType = KeyboardType.Password)
                else -> KeyboardOptions.Default
            }
            , singleLine = true
            , visualTransformation = visualTransformation
        ) { innerTextField ->
            DecorationBox(
                value = value
                , innerTextField = innerTextField
                , enabled = true
                , singleLine = true
                , visualTransformation = visualTransformation
                , interactionSource = interactionSource
                , placeholder = {
                    Text(
                        text = description
                        , style = TextStyle(
                            fontFamily = ibmFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = fontSize.sp
                        )
                        , color = mainDarkGray
                    )
                }
                , colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent
                    , unfocusedContainerColor = Color.Transparent
                )
                , contentPadding = PaddingValues(horizontal = 0.dp, vertical = 12.dp)
            )
        }
    }
}