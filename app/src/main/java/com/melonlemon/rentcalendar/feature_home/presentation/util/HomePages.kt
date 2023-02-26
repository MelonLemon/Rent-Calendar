package com.melonlemon.rentcalendar.feature_home.presentation.util

sealed class HomePages{
    object SchedulePage: HomePages()
    object ExpensesPage: HomePages()
    object BookPage: HomePages()
}
