package com.melonlemon.rentcalendar.feature_analytics.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.feature_analytics.domain.model.BarchartData
import com.melonlemon.rentcalendar.feature_analytics.domain.model.ChartItem
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import org.w3c.dom.Text

@Composable
fun SegmentedBarchart(
    modifier: Modifier = Modifier,
    listSegments: List<ChartItem>,
    ) {
    val sum = listSegments.sumOf { it.value }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .drawBehind {
                var newY = 0f
                listSegments.forEach { item ->
                    val percent = (item.value).toFloat() / sum
                    drawRect(
                        color = item.color,
                        size = this.size.copy(height = this.size.height * percent),
                        topLeft = Offset(x = 0f, y = newY)
                    )
                    newY += this.size.height * percent
                }
            }
    ){

    }

}

@Composable
fun BarchartGraph(
    modifier: Modifier = Modifier,
    boxSize: Size,
    widthBar: Int,
    maxValue: Int,
    data: List<BarchartData>,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = boxSize.width.dp,
                    height = boxSize.width.dp
                )
        ){
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ){
                val part = 4
                val amountDivider = maxValue/part
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)) {

                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        pathEffect = pathEffect
                    )
                }
                NumberLine(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${amountDivider*3}",
                    pathEffect = pathEffect
                )
                NumberLine(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${amountDivider*2}",
                    pathEffect = pathEffect
                )
                NumberLine(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${amountDivider*1}",
                    pathEffect = pathEffect
                )
                NumberLine(
                    modifier = Modifier.fillMaxWidth(),
                    text = "0",
                    pathEffect = pathEffect
                )
            }
            val offsetBarchartContent = 100

            Row(
                modifier
                    .fillMaxSize()
                    .padding(start = offsetBarchartContent.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ){
                repeat(3){ num ->
                    val list = data.map { it.BarchartItem(num) }
                    val sum = list.map { it.value }.sum()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "$sum",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        SegmentedBarchart(
                            modifier = Modifier.size(width = widthBar.dp, height = (sum/maxValue).dp),
                            listSegments = list)
                    }

                }
            }
        }
        val listDescription = data.map { Pair(it.color, it.name + " - " + it.values.joinToString()) }
        ColorCard(
            modifier = Modifier.fillMaxWidth(),
            list = listDescription)
    }
}

@Composable
fun ColorCard(
    modifier: Modifier = Modifier,
    list: List<Pair<Color, String>>
) {
    OutlinedCard(
        modifier = modifier,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            list.forEach { item ->
                ColorStringRow(
                    color = item.first,
                    text = item.second
                )
            }
        }
    }
}

@Composable
fun NumberLine(
    modifier: Modifier = Modifier,
    text: String,
    pathEffect: PathEffect
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ){
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(1.dp)) {

            drawLine(
                color = Color.Red,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect
            )
        }

    }
}

@Composable
fun ColorStringRow(
    modifier: Modifier = Modifier,
    color: Color,
    text: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ){
        Canvas(modifier = Modifier) {
            drawCircle(color, radius = 8.dp.toPx())
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PiecChart(
    modifier: Modifier = Modifier,
    listSegments: List<ChartItem>
) {

}



@Preview(showBackground = true)
@Composable
fun SegmentedBarchartPreview() {
    RentCalendarTheme {
        val listSegments = listOf(
            ChartItem(name = "Direct Cost", value = 300, color = MaterialTheme.colorScheme.tertiaryContainer),
            ChartItem(name = "Indirect Cost", value = 120, color = MaterialTheme.colorScheme.primary),
            ChartItem(name = "Net Income", value = 850, color = MaterialTheme.colorScheme.primaryContainer),
        )
        SegmentedBarchart(
            modifier = Modifier.size(width = 40.dp, height = 200.dp),
            listSegments = listSegments
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BarchartGraphPreview() {
    RentCalendarTheme {
        val data = listOf(
            BarchartData(name = "Direct Cost", values = listOf(300,200,100), color = MaterialTheme.colorScheme.tertiaryContainer),
            BarchartData(name = "Indirect Cost", values = listOf(150,100), color = MaterialTheme.colorScheme.primary),
            BarchartData(name = "Net Income", values = listOf(700, 850, 750), color = MaterialTheme.colorScheme.primaryContainer),
        )
        BarchartGraph(
            boxSize = Size(width = 328f, height = 300f),
            widthBar = 40,
            maxValue = 1000,
            data = data
        )
    }
}

