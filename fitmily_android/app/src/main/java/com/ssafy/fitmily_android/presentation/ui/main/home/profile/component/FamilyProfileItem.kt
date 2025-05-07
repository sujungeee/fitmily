package com.ssafy.fitmily_android.presentation.ui.main.home.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainWhite


@Composable
fun FamilyProfileItem() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileItem()
            Text(
                text = "1966년생 10월 18일  |  여 ",
                style = typography.bodyMedium,
                color = mainDarkGray
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),

            ) {
            Column(
                modifier = Modifier
                    .background(mainWhite, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Text("키/몸무게", style = typography.bodySmall, color = Color.Gray)
                        Text("163cm 30kg", style = typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text("체지방률", style = typography.bodySmall, color = Color.Gray)
                        Text("30%", style = typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    Text("지병", style = typography.bodySmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IllnessItem("고혈압", familyFirst)
                        IllnessItem("당뇨", familyFirst)
                    }
                }
            }
        }
    }
}