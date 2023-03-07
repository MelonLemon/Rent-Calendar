package com.melonlemon.rentcalendar.feature_analytics.presentation.util

sealed class Reports{
    object InvestmentReturn: Reports()
    object CashFlow: Reports()
    object IncomeStatement: Reports()
    object BookedReport: Reports()
}
