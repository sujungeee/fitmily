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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthDto
import com.ssafy.fitmily_android.model.dto.response.home.FamilyHealthResponse
import com.ssafy.fitmily_android.presentation.ui.main.home.component.ProfileItem
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.ProfileUtil
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun FamilyProfileItem(
    item : FamilyHealthDto
) {


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
            ProfileItem(
                sequence = item.userFamilySequence?:1,
                name = item.userNickname,
                animal = item.userZodiacName?:"Mouse",
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = "${item.userBirth}  |  ${if(item.userGender==1){"여"}else{"남"}} ",
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
                        Text("${item.healthHeight.takeIf { it != 0.0 }?.toString() ?: "--"}cm ${item.healthWeight.takeIf { it != 0.0 }?.toString() ?: "--"}kg", style = typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text("BMI", style = typography.bodySmall, color = Color.Gray)
                        Text(item.healthBmi.takeIf { it != 0 }?.toString() ?: "--", style = typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    Text("지병", style = typography.bodySmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (item.healthFiveMajorDiseases.isEmpty() && item.healthOtherDiseases.isEmpty()) {
                        Text("없음", style = typography.bodySmall)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i in item.healthFiveMajorDiseases){
                            IllnessItem(i, ProfileUtil().seqToColor(item.userFamilySequence))
                        }
                        for (i in item.healthOtherDiseases){
                            IllnessItem(i, ProfileUtil().seqToColor(item.userFamilySequence))
                        }
                    }
                }
            }
        }
    }
}