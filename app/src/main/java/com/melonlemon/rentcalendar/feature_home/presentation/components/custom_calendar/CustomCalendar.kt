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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.feature_home.domain.model.SelectedWeekInfo
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.YearMonth
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
    val tempYear by remember{ mutableStateOf(year) }
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val firstScroll = remember{ mutableStateOf(true) }
    val scrollToItem = remember{ mutableStateOf(false) }

    val firstWeekOfCurrentMonth = YearMonth.now().atDay(1)[WeekFields.ISO.weekOfYear()]

    if(scrollToItem.value){
        LaunchedEffect(scrollToItem.value){
            listState.scrollToItem(firstWeekOfCurrentMonth)
        }
        scrollToItem.value = false
        firstScroll.value = false
    }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(11.dp)),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val dateStart = if(tempStartDate != null)
            "${tempStartDate.month.name} ${tempStartDate.dayOfMonth}" else ""
        val dateEnd = if(tempEndDate!= null)
            "${tempEndDate.month.name} ${tempEndDate.dayOfMonth}" else ""

        if(dateStart.isNotBlank() || dateEnd.isNotBlank()){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text="$dateStart - $dateEnd",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(R.string.choose_dates),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

        HeaderWeekView(modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally), cellSize = cellSize)

        LazyColumn(
            state = listState,
            modifier = modifier
                .consumeWindowInsets(contentPadding)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(11.dp)),
            contentPadding = contentPadding,
        ){
            val contentModifier = Modifier
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
        if(firstScroll.value){
            scrollToItem.value = true
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
    contentModifier: Modifier = Modifier,
    density: Density
) {
    println("Month: ${yearMonth.month.name}")
    item(yearMonth.month.name + yearMonth.year + "header") {
        MonthHeader(
            modifier = Modifier.padding(start = 32.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
            month = yearMonth.month.name
        )
    }
    val firstWeekNumber = yearMonth.atDay(1)[WeekFields.ISO.weekOfMonth()]
    val lastWeekNumber = yearMonth.atEndOfMonth()[WeekFields.ISO.weekOfMonth()]
    val firstYearWeekNumber = yearMonth.atDay(1)[WeekFields.ISO.weekOfYear()]
    val lastYearWeekNumber = yearMonth.atEndOfMonth()[WeekFields.ISO.weekOfYear()]
    val listWeeks = (firstWeekNumber..lastWeekNumber).toList()
    val listYearWeeks = (firstYearWeekNumber..lastYearWeekNumber).toList()
    println("bookedDays: $bookedDays")

    itemsIndexed(
        items = listWeeks,
        key = { index, _ ->
            yearMonth.year.toString() +
                    "/" +
                    yearMonth.month.value +
                    "/" +
                    (index + 1).toString()}
    ){ index, week ->
        Box(
            modifier = contentModifier
                .background(MaterialTheme.colorScheme.surface)
                .size(width = (cellSize.width * 7).dp, cellSize.height.dp),

        ) {
            val yearWeek = listYearWeeks[index]

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

            if(selectedDays.isNotEmpty()){

                AnimatedVisibility(
                    visible = selectedDays.containsKey(yearWeek),
                    enter = slideInHorizontally(
                        animationSpec = tween(delayMillis = 1000* (selectedDays[yearWeek]?.index ?: 1) +1000)
                    )  + expandHorizontally (
                        expandFrom = Alignment.Start
                    ) + fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
                ){
                    if(selectedDays.isNotEmpty() && selectedDays.containsKey(yearWeek) && selectedDays[yearWeek]!=null){

                        val startDay = selectedDays[yearWeek]!!.startDate.dayOfWeek.value
                        SelectedWeekView(
                            modifier = contentModifier.padding(start = ((startDay-1)*cellSize.width).dp),
                            cellSize = cellSize,
                            onDayClicked = onDayClicked,
                            selectedDays = selectedDays[yearWeek]!!,
                            yearMonth = yearMonth
                        )
                    }

                }
            }


        }

    }
}



