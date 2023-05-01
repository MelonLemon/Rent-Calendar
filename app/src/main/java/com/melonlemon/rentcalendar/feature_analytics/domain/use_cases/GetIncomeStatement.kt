package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.feature_analytics.domain.model.IncomeStatementInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository

class GetIncomeStatement(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(year:Int, flatId: Int): List<IncomeStatementInfo> {
        val revenueQuarter: Map<Int, Int>
        val monthlyExpQuarter: Map<Int, Int>
        val irregularExpQuarter: Map<Int, Int>
        if(flatId==-1){
            revenueQuarter = repository.getAllRevenueQuarter(year=year)
            monthlyExpQuarter = repository.getAllMonthlyExpensesQuarter(year=year)
            irregularExpQuarter = repository.getAllIrregularExpQuarter(year=year)

        } else {
            revenueQuarter = repository.getRevenueQuarter(year=year, flatId = flatId)
            monthlyExpQuarter = repository.getMonthlyExpensesQuarter(year=year, flatId = flatId)
            irregularExpQuarter = repository.getIrregularExpQuarter(year=year, flatId = flatId)
        }
        val incomeStatementYear = mutableListOf<IncomeStatementInfo>()
        (1..4).forEach { quarter ->
            val revenue = if(revenueQuarter.containsKey(quarter)) revenueQuarter[quarter]?:0 else 0
            val monthlyExpenses = if(monthlyExpQuarter.containsKey(quarter)) monthlyExpQuarter[quarter]?:0 else 0
            val irregularExpenses = if(irregularExpQuarter.containsKey(quarter)) irregularExpQuarter[quarter]?:0 else 0
            incomeStatementYear.add(IncomeStatementInfo(
                quarter = quarter,
                netIncome = revenue - monthlyExpenses- irregularExpenses,
                revenue = revenue,
                monthlyExp = monthlyExpenses,
                irregularExp = irregularExpenses

            ))
        }
        return incomeStatementYear
    }
}