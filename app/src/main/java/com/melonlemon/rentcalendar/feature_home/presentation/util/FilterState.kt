package com.melonlemon.rentcalendar.feature_home.presentation.util

import java.time.YearMonth

data class FilterState(
    val selectedFlatId: Int = -1,
    val yearMonth: YearMonth
)
