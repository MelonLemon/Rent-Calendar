package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay

data class DependState(
    val finResultsDisplay: List<FinResultsDisplay> = emptyList(),
    val schedulePageState: SchedulePageDependState = SchedulePageDependState(),
    val expensesPageState: ExpensesPageDependState = ExpensesPageDependState()
)

