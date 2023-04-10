package com.melonlemon.rentcalendar.feature_home.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.*
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory
import com.melonlemon.rentcalendar.feature_home.presentation.util.NewBookedState
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface HomeRepository {

    suspend fun getIncomeGroupByMY(flatId: Int, year: Int): List<AmountGroupBy>
    suspend fun getExpensesGroupByMY(flatId: Int, year: Int): List<AmountGroupBy>
    suspend fun getAllIncomeGroupByMY(year: Int): List<AmountGroupBy>
    suspend fun getAllExpensesGroupByMY(year: Int): List<AmountGroupBy>
    suspend fun getBookedNightsGroupByMY(flatId: Int, year: Int): List<AmountGroupBy>
    suspend fun getAvgBookedNightsGroupByMY(year: Int): List<AmountGroupBy>
    suspend fun addUpdateFlat(flat: Flats)

    suspend fun updatePaidStatus(id: Int, isPaid: Boolean)
    suspend fun saveBaseOption(flats: List<Flats>, categories: List<Category>)
    suspend fun addNewExpCat(name: String, amount: Int, moneyFlowCategory: MoneyFlowCategory)
    suspend fun getExpCategories(moneyFlowCategory: MoneyFlowCategory): List<Category>
    fun getExpensesByYM(moneyFlowCategory: MoneyFlowCategory, yearMonth: YearMonth, flatId: Int): Flow<List<Expenses>>
    suspend fun updateExpenses(id: Int, amount: Int)
    suspend fun updateCategories(categories: List<CategoryShortInfo>)
    fun getRentList(year: Int, month: Int, flatId: Int): Flow<List<FullRentInfo>>
    suspend fun addNewRent(person: Person, payments: List<Payment>, schedules: List<Schedule>)
    suspend fun addExpenses(yearMonth: YearMonth, flatId: Int, catId: Int, amount: Int, comment: String)
}