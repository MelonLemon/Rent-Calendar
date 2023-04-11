package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.feature_analytics.domain.model.IncomeStatementInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository

class GetIncomeStatement(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(year:Int, flatId: Int): List<IncomeStatementInfo> {
        val revenueQuarter: Map<Int, Int>
        val monthlyExpQuarter: Map<Int, Int>
        val irregExpQuarter: Map<Int, Int>
        if(flatId==-1){
            revenueQuarter = repository.getAllRevenueQuarter(year=year)
            monthlyExpQuarter = repository.getAllMonthlyExpensesQuarter(year=year)
            irregExpQuarter = repository.getAllIrregExpensesQuarter(year=year)

        } else {
            revenueQuarter = repository.getRevenueQuarter(year=year, flatId = flatId)
            monthlyExpQuarter = repository.getMonthlyExpensesQuarter(year=year, flatId = flatId)
            irregExpQuarter = repository.getIrregExpensesQuarter(year=year, flatId = flatId)
        }
        var incomeStatementYear = mutableListOf<IncomeStatementInfo>()
        (1..4).forEach { quarter ->
            val revenue = if(revenueQuarter.containsKey(quarter)) revenueQuarter[quarter]?:0 else 0
            val monthlyExpenses = if(monthlyExpQuarter.containsKey(quarter)) monthlyExpQuarter[quarter]?:0 else 0
            val irregExpenses = if(irregExpQuarter.containsKey(quarter)) irregExpQuarter[quarter]?:0 else 0
            incomeStatementYear.add(IncomeStatementInfo(
                quarter = quarter,
                netIncome = revenue - monthlyExpenses- irregExpenses,
                revenue = revenue,
                monthlyExp = monthlyExpenses,
                irregExp = irregExpenses

            ))
        }
        return incomeStatementYear
    }
}