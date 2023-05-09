package com.melonlemon.rentcalendar.core.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        item{
            BasicCardIcon(
                modifier = Modifier
                    .size(56.dp)
                    .clickable {
                        onCalendarBtnClick()
                    },
                icon = Icons.Filled.DateRange
            )
        }
        item {
            OutlinedTextField(
                value = startDate?.format(format) ?: pattern,
                onValueChange = { },
                readOnly = true,
                placeholder = { Text(text= pattern) },
                maxLines = 1,
                modifier = Modifier.width(110.dp),
                textStyle = MaterialTheme.typography.titleSmall
            )
        }
        item {
            OutlinedTextField(
                value = endDate?.format(format) ?: pattern,
                onValueChange = { },
                readOnly = true,
                placeholder = { Text(text= pattern) },
                maxLines = 1,
                modifier = Modifier.width(110.dp),
                textStyle = MaterialTheme.typography.titleSmall
            )
        }


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
    val listState = rememberLazyListState()
    val scrollToItem = remember{ mutableStateOf(false) }
    val correctYear = remember{ mutableStateOf((tempYear.take(1) == "2" || tempYear.take(1) == "1") && tempYear.length == 4) }

    if(scrollToItem.value){
        LaunchedEffect(scrollToItem.value){
            listState.scrollToItem(yearMonth.monthValue-1)
        }
        scrollToItem.value = false
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showEdit = !showEdit
                }
                .padding(vertical = 16.dp),
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
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val limitNum = 4
                    OutlinedTextField(
                        value = if (tempYear == "0") "" else tempYear,
                        onValueChange = {
                            tempYear = it.take(limitNum)
                            correctYear.value = (tempYear.take(1) == "2" || tempYear.take(1) == "1") && tempYear.length == 4 },
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
                            if (!correctYear.value){
                                Text(
                                    text = stringResource(R.string.not_correct_year),
                                    overflow = TextOverflow.Visible
                                )
                            }



                        }
                    )
                    IconButton(
                        onClick = {
                        if (correctYear.value) {
                            onYearChange(tempYear)
                        }},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if(correctYear.value) MaterialTheme.colorScheme.surfaceColorAtElevation(11.dp) else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            tint = if(correctYear.value) MaterialTheme.colorScheme.primary  else
                                MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                LazyRow(
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val monthList = (1..12).toList()

                    items(
                        items = monthList,
                        key = { monthNum ->
                            Month.of(monthNum).name + monthNum
                        }
                    ) { monthNum ->
                        SFilterButton(
                            text = Month.of(monthNum).name,
                            isSelected = monthNum == yearMonth.monthValue,
                            onBtnClick = { onMonthClick(monthNum) }
                        )
                    }

                }
                scrollToItem.value = true
            }

        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInput(
    modifier:Modifier = Modifier,
    text: String = "",
    onTextChanged: (String) -> Unit,
    onCancelClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onTextChanged,
        shape = MaterialTheme.shapes.small,
        placeholder = { Text(
            text= stringResource(R.string.search),
            color = MaterialTheme.colorScheme.outline
        ) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        trailingIcon = {
            if(text!=""){
                IconButton(
                    modifier = Modifier,
                    onClick = onCancelClicked,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_cancel_24),
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

