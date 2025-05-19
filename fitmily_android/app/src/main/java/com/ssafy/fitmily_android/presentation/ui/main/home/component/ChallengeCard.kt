package com.ssafy.fitmily_android.presentation.ui.main.home.component

import android.icu.text.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.home.ChallengeResponse
import com.ssafy.fitmily_android.ui.theme.familyFirst
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import com.ssafy.fitmily_android.util.DateUtil
import com.ssafy.fitmily_android.util.ProfileUtil
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDate

@Composable
fun ChallengeCard(navController: NavHostController,
                  challengeData :ChallengeResponse) {

    var progressTotal =
        (challengeData.participants[0].distanceCompleted +
                challengeData.participants[1].distanceCompleted +
                challengeData.participants[2].distanceCompleted) / challengeData.targetDistance

    Box{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .background(mainWhite, shape = RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "${DateUtil().getDday(challengeData.startDate)}",
                style = typography.bodyMedium,
                color = familyFirst
            )
            Text(
                text = "${DateUtil().getChallengeDate(challengeData.startDate)}",
                style = typography.bodyMedium,
            )
            Text(
                text = "총 ${challengeData.targetDistance}km 걷기",
                style = typography.bodyLarge,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {

            Spacer(Modifier.size(8.dp))
            Text(
                text = "현재 순위",
                style = typography.bodyMedium,
                color = mainDarkGray
            )
            Spacer(Modifier.size(8.dp))
            LazyColumn {
                items(challengeData.participants.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${index + 1}위",
                            style = typography.bodyMedium,
                        )
                        Spacer(Modifier.size(8.dp))
                        ProfileItem(
                            textStyle = typography.bodyMedium,
                            challengeData.participants[index].familySequence,
                            challengeData.participants[index].nickname,
                            challengeData.participants[index].zodiacName
                        )
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            if (progressTotal < 1) {
                SegmentedProgressBar(
                    segments =
                        listOf(
                            ((challengeData.participants[0].distanceCompleted / challengeData.targetDistance).toFloat()) to ProfileUtil().seqToColor(
                                challengeData.participants[0].familySequence
                            ),
                            ((challengeData.participants[1].distanceCompleted / challengeData.targetDistance).toFloat()) to ProfileUtil().seqToColor(
                                challengeData.participants[1].familySequence
                            ),
                            ((challengeData.participants[2].distanceCompleted / challengeData.targetDistance).toFloat()) to ProfileUtil().seqToColor(
                                challengeData.participants[2].familySequence
                            ),
                            (1-progressTotal).toFloat()  to mainGray
                        ),
                )
            } else {
                SegmentedProgressBar(
                    segments =
                        listOf(
                            1f to Color.Red
                        ),
                )
            }

        }


    }
        if (progressTotal >= 1) {
            Image(
                painter = painterResource(id = R.drawable.completed_icon),
                contentDescription = "completed",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
}
}

