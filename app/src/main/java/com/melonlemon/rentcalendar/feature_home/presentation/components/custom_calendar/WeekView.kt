package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.feature_home.domain.model.SelectedWeekInfo
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

@Composable
internal fun HeaderWeekView(modifier: Modifier = Modifier, cellSize: Size) {
    Row(modifier = modifier) {
        for (day in DayOfWeek.values()) {
            DayOfWeekHeading(
                day = day.name.take(1),
                cellSize = cellSize,
                backgroundColor = Color.Transparent
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekView(
    modifier: Modifier = Modifier,
    cellSize: Size,
    onDayClicked: (LocalDate) -> Unit,
    yearMonth: YearMonth,
    weekNumber: Int,
    bookedDays:List<LocalDate>?=null
) {
    val startWeek = yearMonth.atDay(1)[WeekFields.ISO.weekOfMonth()]

    val plusWeeks = if(startWeek==1) weekNumber.toLong()-1 else weekNumber.toLong()
    val beginningWeek =  yearMonth.atDay(1).plusWeeks(plusWeeks)
    var currentDay = beginningWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    Box(
        modifier = modifier
            .background(Color.Transparent)
            .size(width = (cellSize.width * 7).dp, cellSize.height.dp)) {
        Row(modifier = Modifier) {
            for (i in 0..6) {
                if (currentDay.month == yearMonth.month) {

                    DayView(
                        backgroundColor = Color.Transparent,
                        colorText = if(bookedDays==null || currentDay !in bookedDays)
                        MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant,
                        day = currentDay,
                        cellSize = cellSize,
                        onCellClick = { day ->
                            if(bookedDays==null || currentDay !in bookedDays){
                                onDayClicked(day)
                            }
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(width = cellSize.width.dp, height = cellSize.height.dp)
                            .background(Color.Transparent)
                    )
                }
                currentDay = currentDay.plusDays(1)
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectedWeekView(
    modifier: Modifier = Modifier,
    cellSize: Size,
    onDayClicked: (LocalDate) -> Unit,
    selectedDays:SelectedWeekInfo,
    yearMonth: YearMonth
) {

    val sizeWeek = selectedDays.size
    var currentDay = selectedDays.startDate

    Box(
        modifier = modifier
            .size(width = (cellSize.width * sizeWeek).dp, cellSize.height.dp)) {
        Row(modifier = Modifier) {
            println("Start")
            for (i in 0 until sizeWeek) {
                if(currentDay.month == yearMonth.month){
                    DayView(
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        colorText = MaterialTheme.colorScheme.onPrimary,
                        day = currentDay,
                        cellSize = cellSize,
                        onCellClick = { day ->
                            onDayClicked(day)
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(width = cellSize.width.dp, height = cellSize.height.dp)
                            .background(Color.Transparent)
                    )
                }

                currentDay = currentDay.plusDays(1)
            }
        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SelectedWeekViewPreview() {
    RentCalendarTheme {
        val selectedDays = mapOf(17 to SelectedWeekInfo(1, LocalDate.now(), 2))
        val startDay = selectedDays[17]!!.startDate.dayOfWeek.value
        val cellSize = Size(48f,48f)
        SelectedWeekView(
            modifier = Modifier.padding(start = ((startDay-1)*cellSize.width).dp),
            cellSize = cellSize,
            onDayClicked = { },
            selectedDays = selectedDays[17]!!,
            yearMonth = YearMonth.of(2023,4)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WeekViewPreview() {
    RentCalendarTheme {
        val selectedDays = mapOf(17 to SelectedWeekInfo(1, LocalDate.now(), 2))
        val startDay = selectedDays[17]!!.startDate.dayOfWeek.value
        val cellSize = Size(48f,48f)
        WeekView(
            modifier  = Modifier,
            cellSize=cellSize,
            onDayClicked={ },
            yearMonth= YearMonth.of(2023,4),
            weekNumber=4,
            bookedDays = null
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DayViewViewPreview() {
    RentCalendarTheme {
        val selectedDays = mapOf(17 to SelectedWeekInfo(1, LocalDate.now(), 2))
        val startDay = selectedDays[17]!!.startDate.dayOfWeek.value
        val cellSize = Size(48f,48f)
        DayView(
            backgroundColor = MaterialTheme.colorScheme.primary,
            colorText = MaterialTheme.colorScheme.onPrimary,
            day = LocalDate.now(),
            cellSize = cellSize,
            onCellClick = { }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CrossOverPreview() {
    RentCalendarTheme {
        val selectedDays = mapOf(17 to SelectedWeekInfo(1, LocalDate.now().minusDays(5), 2))
        val startDay = selectedDays[17]!!.startDate.dayOfWeek.value
        val bookedDays = listOf(LocalDate.now(), LocalDate.now().plusDays(1))
        val cellSize = Size(48f,48f)
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .size(width = (cellSize.width * 7).dp, cellSize.height.dp)
        ) {
            WeekView(
                modifier  = Modifier,
                cellSize=cellSize,
                onDayClicked={ },
                yearMonth= YearMonth.of(2023,4),
                weekNumber=4,
                bookedDays = bookedDays
            )

            SelectedWeekView(
                modifier = Modifier.padding(start = ((startDay-1)*cellSize.width).dp),
                cellSize = cellSize,
                onDayClicked = { },
                selectedDays = selectedDays[17]!!,
                yearMonth = YearMonth.of(2023,4)
            )




        }
    }
}