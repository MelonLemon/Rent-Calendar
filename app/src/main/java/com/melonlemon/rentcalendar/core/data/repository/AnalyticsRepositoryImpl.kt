package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.data.util.IRREGULAR_EXP
import com.melonlemon.rentcalendar.core.data.util.REGULAR_EXP
import com.melonlemon.rentcalendar.core.domain.model.AmountGroupBy
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.core.domain.model.MostBookedMonthInfo
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

    override suspend fun getIrregularExpQuarter(flatId: Int, year: Int): Map<Int, Int> {
        return dao.getExpensesQuarter(flatId=flatId, year = year, typeId = IRREGULAR_EXP)
    }

    override suspend fun getAllIrregularExpQuarter(year: Int): Map<Int, Int> {
        return dao.getAllExpensesQuarter(year = year, typeId = IRREGULAR_EXP)
    }

    override suspend fun getRentByDateQuarter(flatId: Int, year: Int): Map<Int, Int> {
        return dao.getPaymentByDateQuarter(flatId=flatId, year = year, isPaid = true)
    }

    override suspend fun getAllRentByDateQuarter(year: Int): Map<Int, Int> {
        return dao.getAllPaymentByDateQuarter(year = year, isPaid = true)
    }

    override suspend fun getExpensesByDateQuarter(flatId: Int, year: Int): Map<Int, List<DisplayInfo>> {
        return dao.getExpensesByDateQuarter(flatId=flatId, year = year)
    }

    override suspend fun getAllExpensesByDateQuarter(year: Int): Map<Int, List<DisplayInfo>> {
        return dao.getAllExpensesByDateQuarter(year = year)
    }

    override suspend fun getBookedPercentYear(flatId: Int, year: Int): Int {
        return dao.getBookedPercentYear(flatId=flatId, year=year)?:0
    }

    override suspend fun getAllBookedPercentYear(year: Int): Int {
        return dao.getAllBookedPercentYear(year=year)?:0
    }

    override suspend fun getAvgDaysRent(flatId: Int, year: Int): Int {
        return dao.getAvgDaysRent(flatId=flatId, year=year)?:0
    }

    override suspend fun getAllAvgDaysRent(year: Int): Int {
        return dao.getAllAvgDaysRent(year=year)?:0
    }

    override suspend fun getAllMostBookedMonth(year: Int): MostBookedMonthInfo? {
        return dao.getAllMostBookedMonth(year=year)
    }

    override suspend fun getAllMostIncomeMonth(year: Int): AmountGroupBy? {
        return dao.getAllMostIncomeMonth(year=year, isPaid = true)
    }

    override suspend fun getGrossRentYearly(year: Int): Int {
        return dao.getGrossRentYearly(year=year)?:0
    }

    override suspend fun getGrossRentYearlyByFlatId(flatId: Int, year: Int): Int {
        return dao.getGrossRentYearlyByFlatId(flatId=flatId, year=year)?:0
    }

    override suspend fun getGrossRentMonthly(year: Int, month: Int): Int {
        return dao.getGrossRentMonthly(year=year,month=month)?:0
    }

    override suspend fun getGrossRentMonthlyByFlatId(flatId: Int, year: Int, month: Int): Int {
        return dao.getGrossRentMonthlyByFlatId(flatId=flatId, year=year,month=month)?:0
    }

    override suspend fun getExpensesYearly(year: Int): Int {
        return dao.getExpensesYearly(year=year)?:0
    }

    override suspend fun getExpensesYearlyByFlatId(flatId: Int, year: Int): Int {
        return dao.getExpensesYearlyByFlatId(flatId=flatId, year=year)?:0
    }

    override suspend fun getExpensesMonthly(year: Int, month: Int): Int {
        return dao.getExpensesMonthly(year=year,month=month)?:0
    }

    override suspend fun getExpensesMonthlyByFlatId(flatId: Int, year: Int, month: Int): Int {
        return dao.getExpensesMonthlyByFlatId(flatId=flatId, year=year,month=month)?:0
    }

    override suspend fun getMostBookedMonth(flatId: Int, year: Int): MostBookedMonthInfo? {
        return dao.getMostBookedMonth(flatId=flatId, year=year)
    }

    override suspend fun getMostIncomeMonth(flatId: Int,year: Int): AmountGroupBy? {
        return dao.getMostIncomeMonth(flatId=flatId, year=year, isPaid = true)
    }
}