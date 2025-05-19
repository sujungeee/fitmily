package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.util.ProfileUtil

@Composable
fun ProfileItem(textStyle: TextStyle = typography.bodyLarge, sequence: Int, name: String, animal: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(ProfileUtil().typeToProfile(animal)?: R.drawable.cow_icon),
            contentDescription = "item",
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .fillMaxWidth(0.1f)
                .aspectRatio(1f)
                .background(ProfileUtil().seqToColor(sequence))
        )
        Spacer(Modifier.size(5.dp))
        Text(
            text = name,
            style = textStyle
        )
    }
}