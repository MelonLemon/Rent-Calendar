package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRange(
    modifier: Modifier = Modifier,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onCalendarBtnClick: () -> Unit
) {
    val pattern = "dd.MM.yyyy"
    val format = DateTimeFormatter.ofPattern(pattern)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        BasicCardIcon(
            modifier = Modifier
                .size(56.dp)
                .clickable {
                    onCalendarBtnClick()
                },
            icon = Icons.Filled.DateRange
        )
        OutlinedTextField(
            value = startDate?.format(format) ?: pattern,
            onValueChange = { },
            readOnly = true,
            placeholder = { Text(text= pattern) },
        )
        OutlinedTextField(
            value = endDate?.format(format) ?: pattern,
            onValueChange = { },
            readOnly = true,
            placeholder = { Text(text= pattern) },
        )

    }
}