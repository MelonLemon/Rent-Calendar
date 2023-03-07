package com.melonlemon.rentcalendar.feature_analytics.presentation.util

import com.melonlemon.rentcalendar.feature_analytics.domain.model.IncomeStatementInfo
import java.time.YearMonth

data class IncomeStatementState(
    val currentMonth: IncomeStatementInfo = IncomeStatementInfo(
        yearMonth = YearMonth.now(),
        netIncome = 0,
        revenue = 0,
        directCost = 0,
        inDirectCost = 0
    ),
    val selectedQt: Int = 1,
)
