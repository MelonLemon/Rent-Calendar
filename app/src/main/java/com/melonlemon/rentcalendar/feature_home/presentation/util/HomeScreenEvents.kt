package com.melonlemon.rentcalendar.feature_home.presentation.util

import androidx.annotation.StringRes
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import java.time.LocalDate
import java.time.YearMonth

sealed class HomeScreenEvents{
    //On Base Save
    data class SendMessage(@StringRes val message: Int): HomeScreenEvents()
    //New Flat
    data class OnNewFlatChanged(val name: String): HomeScreenEvents()
    object OnAddNewFlatBtnClick: HomeScreenEvents()
    data class OnFlatClick(val id: Int): HomeScreenEvents()
    //Home Screen State
    data class OnYearChanged(val year: Int): HomeScreenEvents()
    data class OnMonthClick(val monthInt: Int): HomeScreenEvents()
    //Schedule Page
    data class OnRentPaidChange(val id: Int, val isPaid: Boolean): HomeScreenEvents()
    data class OnYearMonthChange(val yearMonth: YearMonth): HomeScreenEvents()
    //Change Category Expenses
    data class OnNewNameExpCatChanged(val name: String): HomeScreenEvents()
    data class OnNewAmountExpCatChanged(val amount: Int): HomeScreenEvents()
    data class OnAddNewExpCatBtnClick(val moneyFlowCategory: MoneyFlowCategory): HomeScreenEvents()
    data class OnAmountExpChanged(val index: Int, val amount: Int, val monthlyIrregularToggle: Boolean): HomeScreenEvents()
    data class OnExpensesAdd(val catId: Int, val amount: Int, val categoryName: String): HomeScreenEvents()
    data class OnCategoriesChanged(val listChangedCategories: List<ExpensesCategoryInfo>): HomeScreenEvents()
    //New Booked
    data class OnNameBookedChanged(val name: String): HomeScreenEvents()
    data class OnCommentBookedChanged(val comment: String): HomeScreenEvents()
    data class OnDateRangeChanged(val startDate: LocalDate?, val endDate: LocalDate?): HomeScreenEvents()
    data class OnOneNightMoneyChange(val money: Int): HomeScreenEvents()
    data class OnAllMoneyChange(val money: Int): HomeScreenEvents()
    object OnAddNewBookedBtnClick: HomeScreenEvents()
    data class SetCalendarState(val year: Int): HomeScreenEvents()
    //Dialogs
    data class OnExpensesAmountChange(val expensesId: Int, val amount: Int): HomeScreenEvents()
}
