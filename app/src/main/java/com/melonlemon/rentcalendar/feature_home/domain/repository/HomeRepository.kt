package com.melonlemon.rentcalendar.feature_home.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory
import com.melonlemon.rentcalendar.feature_home.presentation.util.NewBookedState
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface HomeRepository {

    suspend fun getFinResultsCurrentYear(flatId: Int): List<FinResultsDisplay>
    suspend fun getFinResultsAllFlatsCurrentYear(): List<FinResultsDisplay>
    suspend fun addNewFlat(name: String)
    suspend fun getAllFlats(): List<CategoryInfo>
    suspend fun updatePaidStatus(id: Int, isPaid: Boolean)
    suspend fun addNewExpCat(name: String, amount: Int, moneyFlowCategory: MoneyFlowCategory)
    fun getExpCategories(moneyFlowCategory: MoneyFlowCategory): Flow<List<ExpensesCategoryInfo>>
    fun getRentList(yearMonth: YearMonth, flatId: Int): Flow<List<RentInfo>>
    suspend fun addNewRent(newBookedState: NewBookedState)
}