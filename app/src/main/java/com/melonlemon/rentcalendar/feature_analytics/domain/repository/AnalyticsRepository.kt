package com.melonlemon.rentcalendar.feature_analytics.domain.repository

interface AnalyticsRepository {
    suspend fun getRevenueQuarter(flatId: Int, year: Int):Map<Int, Int>
    suspend fun getAllRevenueQuarter(year: Int):Map<Int, Int>
    suspend fun getMonthlyExpensesQuarter(flatId: Int, year: Int):Map<Int, Int>
    suspend fun getAllMonthlyExpensesQuarter(year: Int):Map<Int, Int>
    suspend fun getIrregExpensesQuarter(flatId: Int, year: Int):Map<Int, Int>
    suspend fun getAllIrregExpensesQuarter(year: Int):Map<Int, Int>
}