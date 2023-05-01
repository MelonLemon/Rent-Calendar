package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import android.os.Build
import androidx.annotation.RequiresApi
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import java.time.YearMonth

class GetFinResults(
    private val repository: HomeRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(flatId: Int, year: Int): List<FinResultsDisplay> {

        val expensesGrouped = if(flatId==-1)  repository.getAllExpensesGroupByMY(year=year).toMutableList()
        else repository.getExpensesGroupByMY(flatId=flatId, year=year).toMutableList()
        val incomeGrouped = if(flatId==-1)  repository.getAllIncomeGroupByMY(year=year).toMutableList()
        else repository.getIncomeGroupByMY(flatId=flatId, year=year).toMutableList()
        val nights = if(flatId==-1)  repository.getAvgBookedNightsGroupByMY(year=year).toMutableList()
        else repository.getBookedNightsGroupByMY(flatId=flatId, year=year).toMutableList()

        // Create empty year List
        val emptyMonth = FinResultsDisplay(
            flatId = flatId,
            yearMonth = YearMonth.of(year, 1),
            income = 0,
            expenses = 0,
            percentBooked = 0f
        )
        //add 1 as index starts with 0
        val emptyYear = MutableList(12) { month -> emptyMonth.copy(yearMonth=YearMonth.of(year, month+1))}

        // goes through it and add data
        val fullYear = emptyYear.map { finResult ->
            val daysInMonth = finResult.yearMonth.lengthOfMonth()
            var income = 0
            var expenses = 0
            var percentBooked = 0f

            if(incomeGrouped.isNotEmpty() && incomeGrouped[0].month == finResult.yearMonth.monthValue) {
                income = incomeGrouped[0].amount
                incomeGrouped.removeAt(0)
            }
            if(expensesGrouped.isNotEmpty() && expensesGrouped[0].month == finResult.yearMonth.monthValue) {
                expenses = expensesGrouped[0].amount
                expensesGrouped.removeAt(0)
            }
            if(nights.isNotEmpty() && nights[0].month == finResult.yearMonth.monthValue) {
                percentBooked = (nights[0].amount).toFloat()/daysInMonth
                nights.removeAt(0)
            }
            finResult.copy(
                income = income,
                expenses = expenses,
                percentBooked = percentBooked
            )

        }
        return fullYear
}
}