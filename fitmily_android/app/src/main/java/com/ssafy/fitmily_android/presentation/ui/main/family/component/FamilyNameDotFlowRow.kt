package com.ssafy.fitmily_android.presentation.ui.main.family.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.model.dto.response.family.FamilyCalendarMember
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun FamilyNameDotFlowRow(
    families: List<FamilyCalendarMember>,
    modifier: Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        families.forEach { family ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = ProfileUtil().seqToColor(family.userFamilysequence), CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = family.userName,
                    style = Typography.bodyMedium
                )
            }
        }
    }
}