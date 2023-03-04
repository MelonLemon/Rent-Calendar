package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo

data class ExpensesCategoriesState(
    val isRegularMF: Boolean = true,
    val isFixedMF: Boolean = true,
    val newCategoryName: String = "",
    val newCategoryAmount: Int = 0,
    val checkStatusNewCat: CheckStatusStr = CheckStatusStr.UnCheckedStatus
)
