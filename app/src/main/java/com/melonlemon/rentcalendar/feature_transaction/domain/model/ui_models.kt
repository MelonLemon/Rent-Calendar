package com.melonlemon.rentcalendar.feature_transaction.domain.model

import java.time.LocalDate
import java.time.YearMonth

data class TransactionMonth(
    val yearMonth: YearMonth,
    val amount: Int,
    val currencySign: String,
    val daysList: List<AllTransactionsDay>
)

data class AllTransactionsDay(
    val date: LocalDate,
    val transactions: List<TransactionListItem>
) {
    fun queryMatch(text: String) {
        transactions.filter{ it.category.contains(text) || it.comment.contains(text)}
    }
}

data class TransactionListItem(
    val id: Int,
    val category: String,
    val comment: String,
    val amount: Int,
    val currencySign: String
)