package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.InvestmentReturnState
import java.time.LocalDate

class GetInvestmentReturn(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(year: Int, flatId: Int): InvestmentReturnState {
        val month = LocalDate.now().monthValue
        val yearlyGrossRent: Int
        val monthlyGrossRent: Int
        val expensesY: Int
        val expensesM: Int

        if(flatId==-1){
            yearlyGrossRent = repository.getGrossRentYearly(year=year)
            monthlyGrossRent = repository.getGrossRentMonthly(year=year,month=month)
            expensesY = repository.getExpensesYearly(year=year)
            expensesM = repository.getExpensesMonthly(year=year,month=month)

        } else {
            yearlyGrossRent = repository.getGrossRentYearlyByFlatId(year=year, flatId=flatId)
            monthlyGrossRent = repository.getGrossRentMonthlyByFlatId(year=year,month=month, flatId=flatId)
            expensesY = repository.getExpensesYearlyByFlatId(year=year, flatId=flatId)
            expensesM = repository.getExpensesMonthlyByFlatId(year=year,month=month, flatId=flatId)
        }
        return InvestmentReturnState(
            totalPurchasePrice = 0,
            yearlyGrossRent = yearlyGrossRent,
            monthlyGrossRent = monthlyGrossRent,
            netOperatingIncomeM = monthlyGrossRent - expensesM,
            netOperatingIncomeY = yearlyGrossRent - expensesY
        )
    }
}