package com.melonlemon.rentcalendar.feature_transaction.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.TransactionsDay
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getExpensesTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>>
    fun getExpensesTransactionsByFlatId(flatId: List<Int>, year: Int):Flow<Map<Int, List<TransactionsDay>>>
    fun getIncomeTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>>
    fun getIncomeTransactionsFlatId(flatId: List<Int>, year: Int): Flow<Map<Int, List<TransactionsDay>>>
    fun getAllTransactions(year: Int):Flow<Map<Int, List<TransactionsDay>>>
    fun getAllTransactionsByFlatId(flatId: List<Int>, year: Int):Flow<Map<Int, List<TransactionsDay>>>
}