package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory
import kotlinx.coroutines.flow.Flow

class GetExpCategories(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(moneyFlowCategory: MoneyFlowCategory): Flow<List<ExpensesCategoryInfo>> {
        return repository.getExpCategories(moneyFlowCategory)
    }
}