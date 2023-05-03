package com.melonlemon.rentcalendar.feature_transaction.domain.model

import java.time.LocalDate
import java.time.YearMonth

data class TransactionMonth(
    val yearMonth: YearMonth,
    val amount: Int,
    val currencySign: String,
    val daysList: List<AllTransactionsDay>
) {
    fun queryMatch(textSearch: String): List<AllTransactionsDay> {
        return daysList.map {  day ->
            AllTransactionsDay(
                date = day.date,
                transactions = day.queryMatch(textSearch)
            )
        }
    }
    fun getSumOfTransactions(): Int {
        var amount = 0
         daysList.forEach {  day ->
            amount +=day.getSumOfTransactions()
        }
        return amount
    }
}

data class AllTransactionsDay(
    val date: LocalDate,
    val transactions: List<TransactionListItem>
) {
    fun queryMatch(textSearch: String): List<TransactionListItem> {
        return transactions.filter {
            it.category.contains(textSearch) || it.comment.contains(textSearch)
        }
    }
    fun getSumOfTransactions(): Int {
        return transactions.sumOf { it.amount }
    }
}

data class TransactionListItem(
    val id: Int,
    val category: String,
    val comment: String,
    val amount: Int,
    val currencySign: String
)