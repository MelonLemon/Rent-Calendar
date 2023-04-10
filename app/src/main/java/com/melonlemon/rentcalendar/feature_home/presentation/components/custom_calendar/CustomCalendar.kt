package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.feature_home.domain.model.SelectedWeekInfo
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomCalendar(
    modifier: Modifier = Modifier,
    tempStartDate: LocalDate?=null,
    tempEndDate: LocalDate?=null,
    selectedDays: Map<Int, SelectedWeekInfo>,
    cellSize: Size,
    bookedDays: Map<Int, List<LocalDate>>?=null,
    onYearChanged: (Int) -> Unit,
    onDayClick: (LocalDate) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    year: Int
) {
    var tempYear by remember{ mutableStateOf(year) }
    val density = LocalDensity.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text= stringResource(R.string.calendar),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        val dateStart = if(tempStartDate != null)
            "${tempStartDate!!.month.name} ${tempStartDate!!.dayOfMonth}" else ""
        val dateEnd = if(tempEndDate!= null)
            "${tempEndDate!!.month.name} ${tempEndDate!!.dayOfMonth}" else ""
        Text(
            text="$dateStart - $dateEnd",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

        HeaderWeekView(modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally), cellSize = cellSize)
        
        YearWidget(
            year = tempYear,
            onYearChange = onYearChanged,
            density = density
        )
        
        LazyColumn(
            modifier = modifier
                .consumeWindowInsets(contentPadding)
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = contentPadding,
        ){
            val contentModifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)

            (1..12).forEach { monthInt ->
                val yearMonth = YearMonth.of(year, monthInt)
                itemsCalendarMonth(
                    onDayClicked = onDayClick,
                    bookedDays = bookedDays,
                    cellSize = cellSize,
                    yearMonth = yearMonth,
                    contentModifier = contentModifier,
                    density = density,
                    selectedDays = selectedDays
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearWidget(
    year: Int,
    onYearChange: (Int) -> Unit,
    density: Density
) {
    var showEdit by remember { mutableStateOf(false) }
    var tempYear by remember { mutableStateOf(year.toString()) }
    Column(){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showEdit = !showEdit
                }
        ){
            Text(text="$year")
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
            Row(){
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
                        Text(text = if(tempYear.length != 4 || (tempYear.take(1)!="2" && tempYear.take(1)!="1"))
                        stringResource(R.string.not_correct_year) else "")
                    }
                )
                Spacer(modifier = Modifier.width(32.dp))
                IconButton(onClick = {
                    if((tempYear.take(1)=="2" || tempYear.take(1)=="1") && tempYear.length == 4){
                        onYearChange(tempYear.toInt())
                    }

                }) {
                    Icons.Filled.Done

                }
            }
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
private fun LazyListScope.itemsCalendarMonth(
    onDayClicked: (LocalDate) -> Unit,
    bookedDays: Map<Int, List<LocalDate>>?=null,
    selectedDays: Map<Int, SelectedWeekInfo>,
    cellSize: Size,
    yearMonth: YearMonth,
    contentModifier: Modifier,
    density: Density
) {
    item(yearMonth.month.name + yearMonth.year + "header") {
        MonthHeader(
            modifier = Modifier.padding(start = 32.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
            month = yearMonth.month.name
        )
    }
    val firstWeekNumber = yearMonth.atDay(1)[WeekFields.ISO.weekOfMonth()]
    val lastWeekNumber = yearMonth.atEndOfMonth()[WeekFields.ISO.weekOfMonth()]
    val listWeeks = (firstWeekNumber..lastWeekNumber).toList()
    var yearWeek = yearMonth.atDay(1)[WeekFields.ISO.weekOfYear()]

    itemsIndexed(listWeeks){ index, week ->
        Box(
            modifier = contentModifier
                .background(MaterialTheme.colorScheme.surface)
                .size(width = (cellSize.width * 7).dp, cellSize.height.dp)) {

            WeekView(
                modifier  = contentModifier,
                cellSize=cellSize,
                onDayClicked=onDayClicked,
                yearMonth=yearMonth,
                weekNumber=week,
                bookedDays = if(bookedDays!=null)
                    if(bookedDays.containsKey(yearWeek)) bookedDays[yearWeek]
                    else null else null
            )

            //add delay
            AnimatedVisibility(
                visible = selectedDays.containsKey(yearWeek),
                enter = slideInHorizontally(
                    animationSpec = tween(delayMillis = 1000*selectedDays[yearWeek]!!.index+1000)
                ) {
                    with(density) { 40.dp.roundToPx() }
                } + expandHorizontally (
                    expandFrom = Alignment.Start
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
            ){
                val startDay = selectedDays[yearWeek]!!.startDate.dayOfWeek.value
                SelectedWeekView(
                    modifier = contentModifier.padding(start = (startDay*cellSize.width).dp),
                    cellSize = cellSize,
                    onDayClicked = onDayClicked,
                    selectedDays = selectedDays[yearWeek]!!
                )
            }

        }
        yearWeek = yearWeek.plus(1)
    }
}



