import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IndicatorGrid(
    indicators: List<Color>,
    indicatorSize: Int = 8
) {
    val rows = 3
    val cols = 2
    val indicatorList = indicators.take(6)
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0 until cols) {
                    val index = row * cols + col
                    if (index < indicatorList.size) {
                        Box(
                            modifier = Modifier
                                .size(indicatorSize.dp)
                                .clip(CircleShape)
                                .background(indicatorList[index])
                        )
                    } else {
                        Spacer(modifier = Modifier.size(indicatorSize.dp))
                    }
                }
            }
        }
    }
}