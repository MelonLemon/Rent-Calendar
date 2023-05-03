package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import java.time.LocalDate

data class FlatState(
    val newFlat: String = "",
    val listOfFlats: List<CategoryInfo> = emptyList()
)

data class ExpCategoriesState(
    val newCategoryName: String = "",
    val newCategoryAmount: Int = 0,
    val monthlyExpCategories: List<ExpensesCategoryInfo> = emptyList(),
    val irregularExpCategories: List<ExpensesCategoryInfo> = emptyList(),
)

data class NewBookedState(
    val startDate: LocalDate?=null,
    val endDate: LocalDate?=null,
    val name: String="",
    val phone: Int?=null,
    val comment: String="",
    val nights: Int=0,
    val oneNightMoney: Int=0,
    val allMoney: Int=0,
    val isPaid: Boolean = false
)

data class CalendarState(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val year: Int=LocalDate.now().year,
    val bookedDays: Map<Int, List<LocalDate>>?=null
)