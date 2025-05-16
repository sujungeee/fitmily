package com.ssafy.fitmily_android.presentation.ui.main.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.fitmily_android.R
import com.ssafy.fitmily_android.model.dto.response.home.WeatherResponse
import com.ssafy.fitmily_android.ui.theme.mainDarkGray
import com.ssafy.fitmily_android.ui.theme.mainWhite
import okhttp3.internal.wait
import kotlin.math.round

@Composable
fun WeatherCard(
    weather: WeatherResponse?,
    modifier: Modifier
) {

    if(weather == null) {
        Text("날씨 정보를 불러오는 중입니다.")
        return
    }

    val temp = "${round(weather.current.temp - 273.15)}℃"
    val description = weather.current.weather[0].description
    val main = weather.current.weather[0].main
    val iconCode = weather.current.weather[0].icon
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

    Row(modifier = modifier
        .fillMaxWidth()
        .background(mainWhite, shape = RoundedCornerShape(16.dp))
        .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,){

//        Spacer(Modifier.weight(1f))
        AsyncImage(
            model = iconUrl,
            contentDescription = description,
            modifier = Modifier.size(100.dp).weight(0.5f)
        )
//        Spacer(Modifier.weight(1f))

        Text(
            text = mapWeatherMainToKorean(main),
            style = typography.bodyMedium,
            modifier = modifier.weight(1f)
        )

//        Spacer(Modifier.weight(1f))
        Column(
            modifier = modifier.weight(0.5f)
        ) {
            Text(
                text = "현재 기온",
                style = typography.bodyMedium,
                color = mainDarkGray
            )

            Text(
                text = temp
                , style = typography.titleLarge
            )
        }
//        Spacer(Modifier.weight(1f))
    }
}

private fun mapWeatherMainToKorean(main: String): String {

    return when(main) {
        "Thunderstorm" -> "오늘은 뇌우 예보가 있어요.\n실내 운동을 추천드려요!"
        "Drizzle"      -> "이슬비가 내릴 예정이에요.\n우산을 챙기고 가벼운 산책을 해보세요."
        "Rain"         -> "비가 올 예정이에요.\n우산을 꼭 챙기고, 실내 운동을 추천드려요!"
        "Snow"         -> "눈이 내릴 예정이에요.\n미끄럼에 주의하고, 따뜻하게 입으세요."
        "Mist"         -> "안개가 끼어있어요.\n시야가 좋지 않으니 조심하세요."
        "Smoke"        -> "연기가 낀 날씨에요.\n외출을 자제하고, 실내에서 운동해보세요."
        "Haze"         -> "연무가 낀 날씨에요.\n외출 시 마스크를 착용하세요."
        "Dust"         -> "먼지가 많아요.\n외출을 피하고, 실내 운동을 추천드려요!"
        "Fog"          -> "안개가 짙어요.\n운전이나 외출 시 조심하세요."
        "Sand"         -> "모래바람이 예상돼요.\n외출을 삼가고, 실내 운동을 추천드려요!"
        "Ash"          -> "화산재가 날릴 수 있어요.\n실내에서 안전하게 지내세요."
        "Squall"       -> "돌풍이 불 예정이에요.\n야외 활동을 자제하세요."
        "Tornado"      -> "토네이도 주의보가 있어요.\n실내에서 안전하게 머무르세요!"
        "Clear"        -> "맑은 날씨예요.\n야외 운동하기 딱 좋아요!"
        "Clouds"       -> "구름이 많은 날씨예요.\n가벼운 산책이나 운동을 즐겨보세요."
        else           -> "오늘의 날씨 정보를 확인하세요."
    }
}