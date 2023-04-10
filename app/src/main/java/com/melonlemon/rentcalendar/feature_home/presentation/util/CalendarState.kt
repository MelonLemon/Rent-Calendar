package com.melonlemon.rentcalendar.feature_home.presentation.util

import java.time.LocalDate

data class CalendarState(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val year: Int=LocalDate.now().year,
    val bookedDays: Map<Int, List<LocalDate>>?=null
)
