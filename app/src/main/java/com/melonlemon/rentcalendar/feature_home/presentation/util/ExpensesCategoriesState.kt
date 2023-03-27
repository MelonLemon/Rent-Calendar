package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import java.time.YearMonth

data class ExpensesCategoriesState(
    val isRegularMF: Boolean = true,
    val newCategoryName: String = "",
    val newCategoryAmount: Int = 0,
    val checkStatusNewCat: CheckStatusStr = CheckStatusStr.UnCheckedStatus,
)
