package com.melonlemon.rentcalendar.feature_transaction.domain.repository

import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactions(transFilterState: TransFilterState): Flow<List<TransactionMonth>>

}