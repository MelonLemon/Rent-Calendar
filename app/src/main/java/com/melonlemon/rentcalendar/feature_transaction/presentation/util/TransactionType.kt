package com.melonlemon.rentcalendar.feature_transaction.presentation.util

sealed class TransactionType{
    object ExpensesTransaction: TransactionType()
    object IncomeTransaction: TransactionType()
    object AllTransaction: TransactionType()
}
