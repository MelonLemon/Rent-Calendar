package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.data.util.IRREGULAR_EXP
import com.melonlemon.rentcalendar.core.data.util.REGULAR_EXP
import com.melonlemon.rentcalendar.core.domain.model.Category
import com.melonlemon.rentcalendar.core.domain.model.Flats
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository

class SaveBaseOption(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(
        flats: List<String>,
        monthlyExpCat: List<ExpensesCategoryInfo>,
        irregExpCat: List<ExpensesCategoryInfo>
    ): SimpleStatusOperation {
        try {
            val flatsList = flats.map{ flat ->
                Flats(
                    id = -1,
                    name = flat,
                    active = true
                )
            }
            val monthlyCategoryList = monthlyExpCat.map { category ->
                Category(
                    id = -1,
                    typeId = REGULAR_EXP,
                    name = category.name,
                    fixedAmount = category.amount,
                    active = true
                )
            }
            val irregCategoryList = irregExpCat.map { category ->
                Category(
                    id = -1,
                    typeId = IRREGULAR_EXP,
                    name = category.name,
                    fixedAmount = category.amount,
                    active = true
                )
            }
            repository.saveBaseOption(
                flats = flatsList,
                categories = monthlyCategoryList + irregCategoryList
            )
        } catch (e: Exception) {
            return SimpleStatusOperation.OperationFail
        }
        return SimpleStatusOperation.OperationSuccess

    }
}