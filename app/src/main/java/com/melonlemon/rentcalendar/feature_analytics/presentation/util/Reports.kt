package com.melonlemon.rentcalendar.feature_analytics.presentation.util

sealed class Reports{
    object FinSnapShot: Reports()
    object CashFlow: Reports()
    object IncomeStatement: Reports()
    object BalanceSheet: Reports()
}
