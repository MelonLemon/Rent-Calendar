package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.DisplayExpenses
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory

class GetExpCategories(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): DisplayExpenses {

        val monthlyExpCategories = repository.getExpCategories(MoneyFlowCategory.Regular).map { category ->
            ExpensesCategoryInfo(
                id = category.id!!,
                name = category.name,
                subHeader = "",
                amount = category.fixedAmount
            )
        }

        val irregularExpCategories = repository.getExpCategories(MoneyFlowCategory.Irregular).map { category ->
            ExpensesCategoryInfo(
                id = category.id!!,
                name = category.name,
                subHeader = "",
                amount = category.fixedAmount
            )
        }
        return DisplayExpenses(
            monthlyExpCategories = monthlyExpCategories,
            irregularExpCategories = irregularExpCategories)

    }
}