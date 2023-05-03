package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import java.time.LocalDate

data class SchedulePageDependState(
    val rentInfo: List<RentInfo> = emptyList(),
    val schedulePageInfo: SchedulePageInfo = SchedulePageInfo()
)

data class ExpensesPageDependState(
    val monthlyExpenses: List<ExpensesInfo> = emptyList(),
    val irregularExpenses: List<ExpensesInfo> = emptyList(),
)


data class SchedulePageInfo(
    val vacantDays: Int = 0,
    val vacantList: List<Pair<LocalDate, LocalDate>> = emptyList(),
    val amountPaid: Int = 0,
    val amountForecast: Int = 0
)
