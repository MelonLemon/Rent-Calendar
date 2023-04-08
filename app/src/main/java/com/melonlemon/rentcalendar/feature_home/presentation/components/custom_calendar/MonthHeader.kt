package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    month: String,
    height: Dp = 16.dp,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Column {
        Spacer(modifier = Modifier.height(height))
        Text(
            modifier = modifier.padding(start = 12.dp),
            text = month.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = MaterialTheme.typography.titleSmall,
            color = color
        )
    }

}