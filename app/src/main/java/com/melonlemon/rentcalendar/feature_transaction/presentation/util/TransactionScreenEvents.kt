package com.melonlemon.rentcalendar.feature_transaction.presentation.util

sealed class TransactionScreenEvents{
    data class OnSearchTextChanged(val text: String): TransactionScreenEvents()
    object OnCancelClicked: TransactionScreenEvents()
    data class OnTransactionTypeClick(val transactionType: TransactionType): TransactionScreenEvents()
    data class OnFlatsClick(val id: Int): TransactionScreenEvents()
    data class OnMonthClick(val num: Int): TransactionScreenEvents()
    data class OnYearClick(val yearId: Int): TransactionScreenEvents()
    data class OnYearMonthClick(val transactionPeriod: TransactionPeriod): TransactionScreenEvents()

}


