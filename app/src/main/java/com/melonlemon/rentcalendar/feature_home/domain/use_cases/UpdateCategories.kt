package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryShortInfo
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository

class UpdateCategories(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(categories: List<ExpensesCategoryInfo>): SimpleStatusOperation {
        try {
            val categoriesList = categories.map { category ->
                CategoryShortInfo(
                   id = category.id,
                    name = category.name,
                    amount = category.amount
                )
            }
            repository.updateCategories(categoriesList)
        } catch (e: Exception) {
            return SimpleStatusOperation.OperationFail
        }
        return SimpleStatusOperation.OperationSuccess

    }
}