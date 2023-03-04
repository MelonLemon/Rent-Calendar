package com.melonlemon.rentcalendar.feature_home.presentation.util

import java.time.LocalDate

sealed class HomeScreenEvents{
    //New Flat
    data class OnNewFlatChanged(val name: String): HomeScreenEvents()
    object OnAddNewFlatBtnClick: HomeScreenEvents()
    object OnAddNewFlatComplete: HomeScreenEvents()
    data class OnFlatClick(val id: Int): HomeScreenEvents()
    //Home Screen State
    data class OnPageChange(val page: HomePages): HomeScreenEvents()
    //Schedule Page
    data class OnRentPaidChange(val id: Int, val isPaid: Boolean): HomeScreenEvents()
    //Change Category Expenses
    data class OnMoneyFlowChanged(val isRegularMF: Boolean, val isFixedMF: Boolean): HomeScreenEvents()
    data class OnNewNameExpCatChanged(val name: String): HomeScreenEvents()
    data class OnNewAmountExpCatChanged(val amount: Int): HomeScreenEvents()
    object OnAddNewExpCatBtnClick: HomeScreenEvents()
    object OnAddNewExpCatComplete: HomeScreenEvents()
    data class OnAmountExpChanged(val index: Int, val amount: Int): HomeScreenEvents()
    data class OnExpensesAdd(val index: Int, val amount: Int): HomeScreenEvents()
    //New Booked
    object OnCalendarBtnClick: HomeScreenEvents()
    data class OnNameBookedChanged(val name: String): HomeScreenEvents()
    data class OnCommentBookedChanged(val comment: String): HomeScreenEvents()
    data class OnDateRangeChanged(val startDate: LocalDate?, val endDate: LocalDate?): HomeScreenEvents()
    data class OnOneNightMoneyChange(val money: Int): HomeScreenEvents()
    data class OnAllMoneyChange(val money: Int): HomeScreenEvents()
    object OnAddNewBookedBtnClick: HomeScreenEvents()
    object OnAddNewBookedComplete: HomeScreenEvents()
}
