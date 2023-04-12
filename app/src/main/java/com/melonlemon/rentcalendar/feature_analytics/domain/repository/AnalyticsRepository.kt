package com.melonlemon.rentcalendar.feature_analytics.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.AmountGroupBy
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.core.domain.model.MostBookedMonthInfo

interface AnalyticsRepository {
    suspend fun getRevenueQuarter(flatId: Int, year: Int):Map<Int, Int>
    suspend fun getAllRevenueQuarter(year: Int):Map<Int, Int>
    suspend fun getMonthlyExpensesQuarter(flatId: Int, year: Int):Map<Int, Int>
    suspend fun getAllMonthlyExpensesQuarter(year: Int):Map<Int, Int>
    suspend fun getIrregExpensesQuarter(flatId: Int, year: Int):Map<Int, Int>
    suspend fun getAllIrregExpensesQuarter(year: Int):Map<Int, Int>
    suspend fun getRentByDateQuarter(flatId: Int, year: Int):Map<Int, Int>
    suspend fun getAllRentByDateQuarter(year: Int):Map<Int, Int>
    suspend fun getExpensesByDateQuarter(flatId: Int, year: Int):Map<Int, List<DisplayInfo>>
    suspend fun getAllExpensesByDateQuarter(year: Int):Map<Int, List<DisplayInfo>>
    suspend fun getBookedPercentYear(flatId: Int, year: Int):Int
    suspend fun getAllBookedPercentYear(year: Int):Int
    suspend fun getAvgDaysRent(flatId: Int, year: Int): Int
    suspend fun getAllAvgDaysRent(year: Int): Int
    suspend fun getMostBookedMonth(flatId: Int, year: Int): MostBookedMonthInfo
    suspend fun getAllMostBookedMonth(year: Int):MostBookedMonthInfo
    suspend fun getMostIncomeMonth(flatId: Int, year: Int): AmountGroupBy
    suspend fun getAllMostIncomeMonth(year: Int): AmountGroupBy
}