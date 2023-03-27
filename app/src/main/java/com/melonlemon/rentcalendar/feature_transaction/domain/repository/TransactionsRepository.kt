package com.melonlemon.rentcalendar.feature_transaction.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.TransactionsMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactions(transactionIndex: Int, year: Int, month: List<Int>, flatId: Int): Flow<List<TransactionsMonth>>

}