package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import java.time.DayOfWeek

@Composable
internal fun HeaderWeekView(modifier: Modifier = Modifier, cellSize: Size) {
    Row(modifier = modifier) {
        for (day in DayOfWeek.values()) {
            DayOfWeekHeading(day = day.name.take(1), cellSize = cellSize)
        }
    }
}