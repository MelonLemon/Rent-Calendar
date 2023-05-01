package com.melonlemon.rentcalendar.feature_home.presentation.util

import java.time.LocalDate

data class SchedulePageState(
    val vacantDays: Int = 0,
    val vacantList: List<Pair<LocalDate, LocalDate>> = emptyList(),
    val amountPaid: Int = 0,
    val amountForecast: Int = 0
)
