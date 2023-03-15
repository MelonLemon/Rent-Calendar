package com.melonlemon.rentcalendar.core.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.util.TestData
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory
import com.melonlemon.rentcalendar.feature_home.presentation.util.NewBookedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.YearMonth

class HomeRepositoryImpl: HomeRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFinResultsCurrentYear(flatId: Int): List<FinResultsDisplay> {
        val testData = TestData()
        return testData.finResults[flatId]?: emptyList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFinResultsAllFlatsCurrentYear(): List<FinResultsDisplay> {
        val testData = TestData()
        return testData.finResultsAllFlats
    }

    override suspend fun addNewFlat(name: String) {

    }

    override suspend fun getAllFlats(): List<CategoryInfo> {
        val testData = TestData()
        return testData.allflats
    }

    override suspend fun updatePaidStatus(id: Int, isPaid: Boolean) {

    }

    override suspend fun addNewExpCat(name: String, amount: Int, moneyFlowCategory: MoneyFlowCategory) {

    }

    override suspend fun getExpCategories(moneyFlowCategory: MoneyFlowCategory, yearMonth: YearMonth):Map<YearMonth, List<ExpensesCategoryInfo>> {
        return emptyMap()
    }

    override fun getRentList(yearMonth: YearMonth, flatId: Int): Flow<List<RentInfo>> {
        return flowOf()
    }

    override suspend fun addNewRent(newBookedState: NewBookedState) {

    }
}