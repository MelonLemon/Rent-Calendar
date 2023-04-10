package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.use_cases.GetAllFlats

data class HomeUseCases(
    val getFinResults: GetFinResults,
    val addNewFlat: AddNewFlat,
    val updatePaidStatus: UpdatePaidStatus,
    val getRentList: GetRentList,
    val getSchedulePageState: GetSchedulePageState,
    val addNewExpCat: AddNewExpCat,
    val getExpCategories: GetExpCategories,
    val addNewBooked: AddNewBooked,
    val updateCategories: UpdateCategories,
    val addExpenses: AddExpenses,
    val getExpensesByYM: GetExpensesByYM,
    val updateExpenses: UpdateExpenses,
    val saveBaseOption: SaveBaseOption,
    val getBookedDays: GetBookedDays
)
