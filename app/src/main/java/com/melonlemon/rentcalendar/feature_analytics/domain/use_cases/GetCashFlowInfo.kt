package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.model.CashFlowInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository

class GetCashFlowInfo(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(year: Int, flatId: Int): List<CashFlowInfo> {
        val rentQuarter: Map<Int, Int>
        val expensesQuarter: Map<Int, List<DisplayInfo>>

        if(flatId==-1){
            rentQuarter = repository.getAllRentByDateQuarter(year = year)
            expensesQuarter = repository.getAllExpensesByDateQuarter(year = year)
        } else {
            rentQuarter = repository.getRentByDateQuarter(flatId=flatId, year = year)
            expensesQuarter = repository.getExpensesByDateQuarter(flatId=flatId, year = year)
        }
        val cashFlowYear = mutableListOf<CashFlowInfo>()
        (1..4).forEach { quarter ->
            val rent = if(rentQuarter.containsKey(quarter)) rentQuarter[quarter]?:0 else 0
            val expenses = if(expensesQuarter.containsKey(quarter)) expensesQuarter[quarter]?: emptyList() else emptyList()

            cashFlowYear.add(
                CashFlowInfo(
                    quarter = quarter,
                    netCashFlow = rent - expenses.sumOf { it.amount },
                    rent = rent,
                    expenses = expenses
                )
            )
        }
        return cashFlowYear
    }
}