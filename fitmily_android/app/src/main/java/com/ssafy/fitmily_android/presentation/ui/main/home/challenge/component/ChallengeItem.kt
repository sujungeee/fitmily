package com.ssafy.fitmily_android.presentation.ui.main.home.challenge.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainWhite

@Composable
fun ChallengeItem(index: Int = 1) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$index 등",
                style = typography.bodyMedium,
                color = mainDarkGray
            )
            Spacer(modifier = Modifier.size(8.dp))
            ProfileItem()

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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)  {
                        Text("총 거리", style = typography.bodyMedium, color = Color.Gray)
                        Text("3km", style = typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)  {
                        Text("총 산책시간", style = typography.bodyMedium, color = Color.Gray)
                        Text("30분", style = typography.bodyLarge)
                    }
                }
            }
        }
    }
}