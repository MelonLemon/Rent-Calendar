package com.melonlemon.rentcalendar.feature_home.presentation.util

import java.time.YearMonth

data class ScheduleFilterState(
    val selectedFlatId: Int = -1,
    val yearMonth: YearMonth
)
