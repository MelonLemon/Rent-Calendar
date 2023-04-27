package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.AmountGroupBy
import com.melonlemon.rentcalendar.core.domain.model.MostBookedMonthInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.BookedReportState
import java.time.YearMonth

class GetBookedReport(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(year: Int, flatId: Int): BookedReportState {
        val averageBooked: Int
        val averageDayRent: Int
        val mostBookedMonth: MostBookedMonthInfo
        val mostIncomeMonth: AmountGroupBy
        val currentYearMonth = YearMonth.now()
        if(flatId==-1){
            averageBooked = repository.getAllBookedPercentYear(year=year)
            averageDayRent = repository.getAllAvgDaysRent(year=year)
            mostBookedMonth = repository.getAllMostBookedMonth(year=year)
                ?: MostBookedMonthInfo(month=currentYearMonth.monthValue, percent = 0)
            mostIncomeMonth = repository.getAllMostIncomeMonth(year=year)
                ?: AmountGroupBy(year = currentYearMonth.year, month = currentYearMonth.monthValue, amount = 0)
            return BookedReportState(
                averageBooked = averageBooked,
                averageDayRent = averageDayRent,
                mostBookedMonth = mostBookedMonth.month,
                mostBookedMonthPercent = mostBookedMonth.percent,
                mostIncomeMonth = mostIncomeMonth.month,
                mostIncomeMonthAmount = mostIncomeMonth.amount
            )
        } else {
            averageBooked = repository.getBookedPercentYear(flatId=flatId,year=year)
            averageDayRent = repository.getAvgDaysRent(flatId=flatId,year=year)
            mostBookedMonth = repository.getMostBookedMonth(flatId=flatId, year=year)
                ?: MostBookedMonthInfo(month=currentYearMonth.monthValue, percent = 0)
            mostIncomeMonth = repository.getMostIncomeMonth(flatId=flatId, year=year)
                ?: AmountGroupBy(year = currentYearMonth.year, month = currentYearMonth.monthValue, amount = 0)
            return BookedReportState(
                averageBooked = averageBooked,
                averageDayRent = averageDayRent,
                mostBookedMonth = mostBookedMonth.month,
                mostBookedMonthPercent = mostBookedMonth.percent,
                mostIncomeMonth = mostIncomeMonth.month,
                mostIncomeMonthAmount = mostIncomeMonth.amount
            )
        }

    }
}