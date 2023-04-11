package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.data.util.IRREGULAR_EXP
import com.melonlemon.rentcalendar.core.data.util.REGULAR_EXP
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository

class AnalyticsRepositoryImpl(
    private val dao: RentDao
): AnalyticsRepository {
    override suspend fun getRevenueQuarter(flatId: Int, year: Int): Map<Int, Int> {
        return dao.getPaymentQuarter(flatId = flatId, year = year, isPaid = true)
    }

    override suspend fun getAllRevenueQuarter(year: Int): Map<Int, Int> {
        return dao.getAllPaymentQuarter(year = year, isPaid = true)
    }

    override suspend fun getMonthlyExpensesQuarter(flatId: Int, year: Int): Map<Int, Int> {
        return dao.getExpensesQuarter(flatId=flatId, year = year, typeId = REGULAR_EXP)
    }

    override suspend fun getAllMonthlyExpensesQuarter(year: Int): Map<Int, Int> {
        return dao.getAllExpensesQuarter(year = year, typeId = REGULAR_EXP)
    }

    override suspend fun getIrregExpensesQuarter(flatId: Int, year: Int): Map<Int, Int> {
        return dao.getExpensesQuarter(flatId=flatId, year = year, typeId = IRREGULAR_EXP)
    }

    override suspend fun getAllIrregExpensesQuarter(year: Int): Map<Int, Int> {
        return dao.getAllExpensesQuarter(year = year, typeId = IRREGULAR_EXP)
    }
}