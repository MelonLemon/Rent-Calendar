package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.data.util.*
import com.melonlemon.rentcalendar.core.domain.model.*
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

class HomeRepositoryImpl(
    private val dao: RentDao
): HomeRepository {
    override suspend fun getIncomeGroupByMY(flatId: Int, year: Int): List<AmountGroupBy> {
        return dao.getPaymentGroupByMY(flatId = flatId, year = year, isPaid = true)
    }

    override suspend fun getExpensesGroupByMY(flatId: Int, year: Int): List<AmountGroupBy> {
        return dao.getExpensesGroupByMY(flatId = flatId, year = year)
    }

    override suspend fun getAllIncomeGroupByMY(year: Int): List<AmountGroupBy> {
        return dao.getAllPaymentGroupByMY(year = year, isPaid = true)
    }

    override suspend fun getAllExpensesGroupByMY(year: Int): List<AmountGroupBy> {
        return dao.getAllExpensesGroupByMY(year = year)
    }

    override suspend fun getBookedNightsGroupByMY(flatId: Int, year: Int): List<AmountGroupBy> {
        return dao.getBookedNightsGroupByMY(flatId = flatId, year = year)
    }

    override suspend fun getAvgBookedNightsGroupByMY(year: Int): List<AmountGroupBy> {
        return dao.getAvgBookedNightsGroupByMY(year = year)
    }

    override suspend fun addUpdateFlat(flat: Flats) {
        dao.addFlat(flat = flat)
    }

    override suspend fun getBookedDays(year: Int, flatId: Int): Map<Int, List<LocalDate>>? {
        return dao.getBookedDaysByWeek(year = year, flatId=flatId)
    }

    override suspend fun updatePaidStatus(id: Int, isPaid: Boolean) {
        dao.updatePaymentStatus(id = id, isPaid = isPaid)
    }



    override suspend fun addNewExpCat(name: String, amount: Int, moneyFlowCategory: MoneyFlowCategory) {
        val typeId = when(moneyFlowCategory){
            MoneyFlowCategory.Regular -> {
                REGULAR_EXP
            }
            MoneyFlowCategory.Irregular -> {
                IRREGULAR_EXP
            }
        }
        dao.addCategory(Category(
            id = null,
            typeId = typeId,
            name = name,
            fixedAmount = amount,
            active = true
        ))
    }

    override suspend fun getExpCategories(moneyFlowCategory: MoneyFlowCategory): List<Category> {
        val typeId = when(moneyFlowCategory){
            MoneyFlowCategory.Regular -> {
                REGULAR_EXP
            }
            MoneyFlowCategory.Irregular-> {
                IRREGULAR_EXP
            }
        }
        return dao.getCategoriesByTypeId(typeId)
    }

    override fun getExpensesByYM(
        moneyFlowCategory: MoneyFlowCategory,
        yearMonth: YearMonth,
        flatId: Int
    ): Flow<List<Expenses>> {
        val typeId = when(moneyFlowCategory){
            MoneyFlowCategory.Regular -> {
                REGULAR_EXP
            }
            MoneyFlowCategory.Irregular-> {
                IRREGULAR_EXP
            }
        }
        return dao.getExpensesByTypeId(
            flatId = flatId,
            year = yearMonth.year,
            month = yearMonth.monthValue,
            typeId = typeId
        )
    }

    override suspend fun updateExpenses(id: Int, amount: Int) {
        dao.updateExpenses(
            id = id,
            amount = amount
        )
    }

    override suspend fun updateCategories(categories: List<CategoryShortInfo>) {
        dao.updateCategories(categories=categories)
    }

    override fun getRentList(year: Int, month: Int, flatId: Int): Flow<List<FullRentInfo>> {
        return dao.getFullRentInfoByYM(
            year = year,
            month = month,
            flatId = flatId
        )
    }

    override suspend fun addNewRent(person: Person, payments: List<Payment>, schedules: List<Schedule>) {
        dao.addSchedules(
            person = person,
            payments = payments,
            schedules = schedules
        )
    }

    override suspend fun addExpenses(yearMonth: YearMonth, flatId: Int, catId: Int, amount: Int, comment: String) {
        dao.addExpenses(
            Expenses(
                id = null,
                flatId = flatId,
                year = yearMonth.year,
                month = yearMonth.monthValue,
                categoryId = catId,
                amount = amount,
                paymentDate = LocalDate.now(),
                comment = comment
            )
        )
    }
}