package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import com.melonlemon.rentcalendar.feature_home.domain.model.SelectedWeekInfo
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters

fun getSelectedDatesList(
    startDate: LocalDate?,
    endDate: LocalDate?
): Map<Int, SelectedWeekInfo>{
    println("startDate: $startDate, endDate: $endDate")
    if(startDate==null){
        return emptyMap()
    }

    if(endDate==null){
        val startWeekNumber = startDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
        return mapOf(startWeekNumber to SelectedWeekInfo(index = 0, startDate = startDate, size = 1))
    } else {
        val startWeekNumber = startDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
        val endWeekNumber = endDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
        val selections = mutableMapOf<Int, SelectedWeekInfo>()
        (startWeekNumber..endWeekNumber).forEachIndexed{ index, week->
            val startWeekNum = if(week==startWeekNumber) startDate.dayOfWeek.value else 1
            val endWeekNum = if(week==endWeekNumber) endDate.dayOfWeek.value else 7
            println("startWeekNum: $startWeekNum, endWeekNum: $endWeekNum")
            //Add 1 as to make it inclusive
            val sizeOfSelection = endWeekNum - startWeekNum + 1
            println("sizeOfSelection: $sizeOfSelection")
            val currentWeekDate = startDate.plusWeeks(index.toLong())
            val mondayDate = currentWeekDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val newStartDate = if(week==startWeekNumber) startDate else mondayDate
            selections[week] =
                SelectedWeekInfo(index = index, startDate = newStartDate, size = sizeOfSelection)
        }
        return selections
    }
}