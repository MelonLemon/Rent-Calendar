package com.melonlemon.rentcalendar.feature_transaction.domain.use_cases

data class TransactionsUseCases(
    val getTransactions: GetTransactions,
    val getFilteredTransactions: GetFilteredTransactions
)
