package com.melonlemon.rentcalendar.feature_home.domain.use_cases

data class HomeUseCases(
    val getFinResults: GetFinResults,
    val addNewFlat: AddNewFlat,
    val updatePaidStatus: UpdatePaidStatus,
    val getRentList: GetRentList,
    val getSchedulePageInfo: GetSchedulePageInfo,
    val addNewExpCat: AddNewExpCat,
    val getExpCategories: GetExpCategories,
    val addNewBooked: AddNewBooked,
    val updateCategories: UpdateCategories,
    val addExpenses: AddExpenses,
    val getExpensesByYM: GetExpensesByYM,
    val updateExpenses: UpdateExpenses,
    val getBookedDays: GetBookedDays
)
