package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory

class GetExpCategories(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Pair<List<ExpensesCategoryInfo>, List<ExpensesCategoryInfo>> {

        val categoriesReg = repository.getExpCategories(MoneyFlowCategory.Regular).map { category ->
            ExpensesCategoryInfo(
                id = category.id!!,
                name = category.name,
                subHeader = "",
                amount = category.fixedAmount
            )
        }

        val categoriesIr = repository.getExpCategories(MoneyFlowCategory.Irregular).map { category ->
            ExpensesCategoryInfo(
                id = category.id!!,
                name = category.name,
                subHeader = "",
                amount = category.fixedAmount
            )
        }
        return Pair(categoriesReg, categoriesIr)

    }
}