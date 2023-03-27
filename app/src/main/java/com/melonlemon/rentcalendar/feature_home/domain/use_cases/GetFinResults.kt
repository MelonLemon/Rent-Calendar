package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.util.toRange
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import java.time.YearMonth

class GetFinResults(
    private val repository: HomeRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(flatId: Int): List<FinResultsDisplay> {

//        val finResults = if(flatId==-1) repository.getFinResultsAllFlatsCurrentYear() else
//            repository.getFinResultsCurrentYear(flatId)
//        if(finResults.isNotEmpty()){
//            val emptyFinResults = FinResultsDisplay(
//                flatId = finResults[0].flatId,
//                flatName = finResults[0].flatName,
//                yearMonth = YearMonth.now(),
//                income = 0,
//                expenses = 0,
//                percentBooked = 0f
//            )
//            val finalList = mutableListOf<FinResultsDisplay>()
//            var yearMonthFirst = YearMonth.of(YearMonth.now().year, 1)
//            if(yearMonthFirst != finResults[0].yearMonth){
//                while(yearMonthFirst != finResults[0].yearMonth){
//                    finalList.add(emptyFinResults.copy(yearMonth = yearMonthFirst))
//                    yearMonthFirst = yearMonthFirst.plusMonths(1)
//                }
//            }
//
//            finResults.forEach { finResult ->
//                var yearMonthLast = if(finalList.isEmpty()) yearMonthFirst else  finalList.last().yearMonth.plusMonths(1)
//                if(yearMonthLast != finResult.yearMonth){
//                    while(yearMonthLast != finResult.yearMonth){
//                        finalList.add(emptyFinResults.copy(yearMonth = yearMonthLast))
//                        yearMonthLast = yearMonthFirst.plusMonths(1)
//                    }
//                }
//                finalList.add(finResult)
//            }
//            return finalList
//        } else return finResults
        return emptyList()
    }
}