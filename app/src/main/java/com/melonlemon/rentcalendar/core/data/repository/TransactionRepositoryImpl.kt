package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class TransactionRepositoryImpl(
    private val dao: RentDao
): TransactionsRepository {
    override fun getTransactions(transFilterState: TransFilterState): Flow<List<TransactionMonth>> {
        return emptyFlow()
    }
}