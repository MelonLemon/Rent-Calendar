package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.feature_home.domain.model.YearWeek
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
    cellSize: Size,
    bookedDays: Map<YearWeek, List<LocalDate>>?=null,
    onDayClick: (LocalDate) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    year: Int
) {
    var tempYear by remember{ mutableStateOf(year) }
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
            onYearClick = { },
            onYearChange = { }
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
                )
            }

        }
    }
}

private fun getNumberOfMonths(yearMonth: YearMonth, weekFields: WeekFields): Int
{
    val firstWeekNumber = yearMonth.atDay(1)[weekFields.weekOfMonth()]
    val lastWeekNumber = yearMonth.atEndOfMonth()[weekFields.weekOfMonth()]
    return lastWeekNumber - firstWeekNumber + 1
}

@Composable
fun YearWidget(year: Int, onYearClick: () -> Unit, onYearChange: () -> Unit) {

}

@RequiresApi(Build.VERSION_CODES.O)
private fun LazyListScope.itemsCalendarMonth(
    onDayClicked: (LocalDate) -> Unit,
    bookedDays: Map<YearWeek, List<LocalDate>>?=null,
    cellSize: Size,
    yearMonth: YearMonth,
    contentModifier: Modifier
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
    itemsIndexed(listWeeks){ index, week ->
        //Week View - base
        //Week View - booked
        //Week View - new booked
    }


}

