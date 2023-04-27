package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo

data class DisplayExpenses(
    val monthlyExpCategories: List<ExpensesCategoryInfo> = emptyList(),
    val irregularExpCategories: List<ExpensesCategoryInfo> = emptyList()
)
