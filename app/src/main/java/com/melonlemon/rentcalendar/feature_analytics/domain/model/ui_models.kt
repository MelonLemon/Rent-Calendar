package com.melonlemon.rentcalendar.feature_analytics.domain.model

import androidx.compose.runtime.Composable
import java.time.YearMonth

data class IncomeStatementInfo(
    val yearMonth: YearMonth,
    val netIncome: Int,
    val revenue: Int,
    val directCost: Int,
    val inDirectCost: Int
)

data class ExpensesInfo(
    val id: Int,
    val name: String,
    val amount: Int
)

data class CashFlowInfo(
    val yearMonth: YearMonth,
    val netCashFlow: Int,
    val rent: Int,
    val expenses: List<ExpensesInfo>
)