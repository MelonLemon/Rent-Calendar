package com.melonlemon.rentcalendar.feature_analytics.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import com.melonlemon.rentcalendar.feature_analytics.domain.model.ChartItem


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





