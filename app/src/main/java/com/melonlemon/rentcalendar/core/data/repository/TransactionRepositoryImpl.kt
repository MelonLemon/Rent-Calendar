package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.domain.model.TransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImpl(
    private val dao: RentDao
): TransactionsRepository {
    override fun getExpensesTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>>{
        return dao.getExpensesTransactions(year=year)
    }

    override fun getExpensesTransactionsByFlatId(
        flatId: List<Int>,
        year: Int
    ): Flow<Map<Int, List<TransactionsDay>>> {
        return dao.getExpensesTransactionsByFlatId(flatId=flatId,year=year)
    }

    override fun getIncomeTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>> {
        return dao.getIncomeTransactions(year=year)
    }

    override fun getIncomeTransactionsFlatId(
        flatId: List<Int>,
        year: Int
    ): Flow<Map<Int, List<TransactionsDay>>> {
        return dao.getIncomeTransactionsFlatId(flatId=flatId,year=year)
    }

    override fun getAllTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>> {
        return dao.getAllTransactions(year=year)
    }

    override fun getAllTransactionsByFlatId(flatId: List<Int>, year: Int): Flow<Map<Int, List<TransactionsDay>>> {
        return dao.getAllTransactionsByFlatId(flatId=flatId, year=year)
    }

}