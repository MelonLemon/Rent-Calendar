package com.melonlemon.rentcalendar.feature_analytics.presentation.util

import com.melonlemon.rentcalendar.feature_analytics.domain.model.CashFlowInfo
import java.time.YearMonth

data class CashFlowState(
    val currentCashFlow: CashFlowInfo = CashFlowInfo(
        netCashFlow = 0,
        yearMonth = YearMonth.now(),
        rent = 0,
        expenses = emptyList()
    ),
    val selectedQt: Int = 1
)
