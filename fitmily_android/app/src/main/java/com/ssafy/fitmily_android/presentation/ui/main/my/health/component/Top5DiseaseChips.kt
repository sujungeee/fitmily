package com.ssafy.fitmily_android.presentation.ui.main.my.health.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.ui.theme.Typography
import com.ssafy.fitmily_android.ui.theme.mainBlue
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.ui.theme.secondaryBlue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Top5DiseaseChips(
    top5Diseases: List<String>,
    selectedDiseases: Set<String>,
    onDiseaseSelected: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        top5Diseases.forEach { disease ->

            val isSelected = selectedDiseases.contains(disease)

            FilterChip(
                selected = isSelected,
                onClick = { onDiseaseSelected(disease) },
                label = {
                    Text(
                        text = disease,
                        style = Typography.bodyMedium
                    )
                },
                shape = RoundedCornerShape(16.dp),
                border = null,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = secondaryBlue,
                    labelColor = mainBlue,
                    selectedContainerColor = mainBlue,
                    selectedLabelColor = mainWhite
                )
            )
        }
    }
}