package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
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
    yearMonth: YearMonth,
    onYearChange: (String) -> Unit,
    onMonthClick: (Int) -> Unit
) {

    var showEdit by remember { mutableStateOf(false) }
    var tempYear by remember{ mutableStateOf(yearMonth.year.toString()) }
    val density = LocalDensity.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showEdit = !showEdit
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null)
            Text(
                text = "${yearMonth.month.name} ${yearMonth.year}",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Left
            )
        }
        AnimatedVisibility(
            visible = showEdit,
            enter = slideInVertically {
                with(density) { 40.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ){
            Column {
                Row() {
                    val limitNum = 4
                    OutlinedTextField(
                        value = if (tempYear == "0") "" else tempYear,
                        onValueChange = { tempYear = it.take(limitNum) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = MaterialTheme.typography.titleMedium,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            textColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.width(90.dp),
                        supportingText = {
                            Text(
                                text = if (tempYear.length != 4 || (tempYear.take(1) != "2" && tempYear.take(
                                        1
                                    ) != "1")
                                )
                                    stringResource(R.string.not_correct_year) else ""
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    IconButton(onClick = {
                        if ((tempYear.take(1) == "2" || tempYear.take(1) == "1") && tempYear.length == 4) {
                            onYearChange(tempYear)
                        }

                    }) {
                        Icons.Filled.Done

                    }
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val monthList = (1..12).toList()

                    items(
                        items = monthList,
                        key = { monthNum ->
                            Month.of(monthNum).name + monthNum
                        }
                    ) { monthNum ->
                        SearchFilterButton(
                            text = Month.of(monthNum).name,
                            isSelected = monthNum == yearMonth.monthValue,
                            onBtnClick = { onMonthClick(monthNum) }
                        )

                    }
                }
            }

        }

    }

}