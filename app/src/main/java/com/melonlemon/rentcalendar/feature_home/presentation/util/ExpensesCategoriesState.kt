package com.melonlemon.rentcalendar.feature_home.presentation.util


data class ExpensesCategoriesState(
    val isRegularMF: Boolean = true,
    val newCategoryName: String = "",
    val newCategoryAmount: Int = 0
)
