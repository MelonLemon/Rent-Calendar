package com.melonlemon.rentcalendar.feature_transaction.presentation.util

sealed class TransactionPeriod{
    object YearPeriod: TransactionPeriod()
    object MonthsPeriod: TransactionPeriod()
}
