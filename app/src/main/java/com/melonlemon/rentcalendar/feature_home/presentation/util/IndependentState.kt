package com.melonlemon.rentcalendar.feature_home.presentation.util

data class IndependentState(
    val flatState: FlatState = FlatState(),
    val expCategoriesState: ExpCategoriesState = ExpCategoriesState(),
    val newBookedState: NewBookedState = NewBookedState(),
    val calendarState: CalendarState = CalendarState(),
    val currencySign: String = ""
)
