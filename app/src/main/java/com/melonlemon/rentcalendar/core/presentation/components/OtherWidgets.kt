package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionPeriod
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType
import java.time.LocalDate
import java.time.Month
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
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = endDate?.format(format) ?: pattern,
            onValueChange = { },
            readOnly = true,
            placeholder = { Text(text= pattern) },
            modifier = Modifier.weight(1f)
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearMonthRow(
    modifier: Modifier = Modifier,
    chosenMonthNum: Int,
    year: Int,
    onYearChange: (String) -> Unit,
    onMonthClick: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        val limitNum = 4
        OutlinedTextField(
            value = if (year == 0) "" else "$year",
            onValueChange = { onYearChange(it.take(limitNum)) },
            placeholder = { Text(text= stringResource(R.string.number_placeholder)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.width(90.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            val monthList = (1..12).toList()

            itemsIndexed(monthList){ index, monthNum ->
                SearchFilterButton(
                    text = Month.of(monthNum).name,
                    isSelected = monthNum == chosenMonthNum,
                    onBtnClick = { onMonthClick(monthNum) }
                )

            }
        }

    }
}