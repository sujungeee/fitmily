package com.ssafy.fitmily_android.presentation.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.backGroundGray
import com.ssafy.fitmily_android.ui.theme.mainBlack
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun GenderSelector (
    selectedGender: String
    , onGenderSelected: (String) -> Unit
) {
    val genders = listOf("남", "여")

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        genders.forEach { gender ->
            val isSelected = gender == selectedGender

            Surface(
                modifier = Modifier
                    .height(36.dp)
                    .weight(1f)
                , shape = RoundedCornerShape(16.dp)
                , color = if (isSelected) mainBlue else backGroundGray
                , border = if (isSelected) null else BorderStroke(1.dp, mainBlack)
                , onClick = { onGenderSelected(gender) }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                    , contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = gender
                        , color = if (isSelected) mainWhite else mainBlack
                        , style = Typography.bodyMedium
                    )
                }
            }
        }
    }
}