package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesInfo
import java.time.LocalDate
import java.time.YearMonth

sealed class HomeScreenEvents{
    //On Base Save
    object CloseErrorMessage: HomeScreenEvents()
    object RefreshFailAttempt: HomeScreenEvents()
    //New Flat
    data class OnNewFlatChanged(val name: String): HomeScreenEvents()
    object OnAddNewFlatBtnClick: HomeScreenEvents()
    object OnAddNewFlatComplete: HomeScreenEvents()
    data class OnFlatClick(val id: Int): HomeScreenEvents()
    //Home Screen State
    data class OnPageChange(val page: HomePages): HomeScreenEvents()
    data class OnYearChanged(val year: Int): HomeScreenEvents()
    data class OnMonthClick(val monthInt: Int): HomeScreenEvents()
    //Schedule Page
    data class OnRentPaidChange(val id: Int, val isPaid: Boolean): HomeScreenEvents()
    data class OnYearMonthChange(val yearMonth: YearMonth): HomeScreenEvents()
    //Change Category Expenses
    data class OnMoneyFlowChanged(val isRegularMF: Boolean): HomeScreenEvents()
    data class OnNewNameExpCatChanged(val name: String): HomeScreenEvents()
    data class OnNewAmountExpCatChanged(val amount: Int): HomeScreenEvents()
    object OnAddNewExpCatBtnClick: HomeScreenEvents()
    object OnAddNewExpCatComplete: HomeScreenEvents()
    data class OnAmountExpChanged(val index: Int, val amount: Int): HomeScreenEvents()
    data class OnExpensesAdd(val catId: Int, val amount: Int, val categoryName: String): HomeScreenEvents()
    data class OnSelectExpensesChange(val expensesInfo: ExpensesInfo): HomeScreenEvents()
    object OnExpensesUpdateComplete: HomeScreenEvents()
    data class OnCategoriesChanged(val listChangedCategories: List<ExpensesCategoryInfo>): HomeScreenEvents()
    //New Booked
    data class OnNameBookedChanged(val name: String): HomeScreenEvents()
    data class OnCommentBookedChanged(val comment: String): HomeScreenEvents()
    data class OnDateRangeChanged(val startDate: LocalDate?, val endDate: LocalDate?): HomeScreenEvents()
    data class OnOneNightMoneyChange(val money: Int): HomeScreenEvents()
    data class OnAllMoneyChange(val money: Int): HomeScreenEvents()
    object OnAddNewBookedBtnClick: HomeScreenEvents()
    data class SetCalendarState(val year: Int): HomeScreenEvents()
    object OnAddNewBookedComplete: HomeScreenEvents()
    //Dialogs
    data class OnCurrencySignChanged(val sign: String): HomeScreenEvents()
    data class OnExpensesAmountChange(val expensesId: Int, val amount: Int): HomeScreenEvents()
}
