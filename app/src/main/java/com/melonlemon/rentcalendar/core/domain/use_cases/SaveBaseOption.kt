package com.melonlemon.rentcalendar.core.domain.use_cases

import com.melonlemon.rentcalendar.core.data.util.IRREGULAR_EXP
import com.melonlemon.rentcalendar.core.data.util.REGULAR_EXP
import com.melonlemon.rentcalendar.core.domain.model.Category
import com.melonlemon.rentcalendar.core.domain.model.CategoryType
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.core.domain.model.Flats
import com.melonlemon.rentcalendar.core.domain.repository.CoreRentRepository
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo

class SaveBaseOption(
    private val repository: CoreRentRepository
) {
    suspend operator fun invoke(
        flats: List<String>,
        monthlyExpCat: List<DisplayInfo>,
        irregExpCat: List<DisplayInfo>
    ): SimpleStatusOperation {
        try{
            repository.addCategoryType(
                CategoryType(
                    id = REGULAR_EXP,
                    isRegular = true,
                )
            )

            repository.addCategoryType(
                CategoryType(
                    id = IRREGULAR_EXP,
                    isRegular = false,
                )
            )
        } catch (e: Exception) {
            return SimpleStatusOperation.OperationFail
        }

        try {
            val flatsList = flats.map{ flat ->
                Flats(
                    id = null,
                    name = flat,
                    active = true
                )
            }
            val monthlyCategoryList = monthlyExpCat.map { category ->
                Category(
                    id = null,
                    typeId = REGULAR_EXP,
                    name = category.name,
                    fixedAmount = category.amount,
                    active = true
                )
            }
            val irregCategoryList = irregExpCat.map { category ->
                Category(
                    id = null,
                    typeId = IRREGULAR_EXP,
                    name = category.name,
                    fixedAmount = category.amount,
                    active = true
                )
            }
            val allCategories = monthlyCategoryList + irregCategoryList
            println("All cayegories List: $allCategories")
            repository.saveBaseOption(
                flats = flatsList,
                categories = allCategories
            )
        } catch (e: Exception) {
            return SimpleStatusOperation.OperationFail
        }
        return SimpleStatusOperation.OperationSuccess

    }
}