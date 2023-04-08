package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DayCellContainer(
    modifier: Modifier =Modifier,
    cellSize: Size,
    onCellClick: () -> Unit = { },
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(width = cellSize.width.dp, height = cellSize.height.dp)
            .pointerInput(Any()) {
                detectTapGestures {
                    onCellClick()
                }
            }
            .background(backgroundColor)
    ) {
        content()
    }
}

@Composable
internal fun DayOfWeekHeading(
    day: String,
    cellSize: Size,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    colorText: Color = MaterialTheme.colorScheme.onSurface,
) {
    DayCellContainer(
        cellSize =  cellSize,
        backgroundColor = backgroundColor
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            text = day,
            style = MaterialTheme.typography.bodySmall,
            color = colorText
        )
    }
}