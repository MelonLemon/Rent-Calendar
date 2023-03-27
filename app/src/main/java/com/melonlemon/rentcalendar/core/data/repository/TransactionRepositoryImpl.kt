package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.domain.model.TransactionsMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class TransactionRepositoryImpl(
    private val dao: RentDao
): TransactionsRepository {
    override fun getTransactions(transactionIndex: Int, year: Int, month: List<Int>, flatId: Int): Flow<List<TransactionsMonth>> {

        when{
            //All Transactions
            transactionIndex == -1 && month.isEmpty() && flatId == -1 -> {
                return dao.getAllTransactions(year = year)
            }
            transactionIndex == -1 && month.isEmpty() && flatId != -1 -> {
                return dao.getAllTransactionsByFlatId(year = year, flatId = flatId)
            }
            transactionIndex == -1 && month.isNotEmpty() && flatId == -1 -> {
                return dao.getAllTransactionsMonth(year = year, months = month)
            }
            transactionIndex == -1 && month.isNotEmpty() && flatId != -1 -> {
                return dao.getAllTransactionsByFlatIdM(year = year, flatId = flatId, months = month)
            }
            //Income
            transactionIndex == 0 && month.isEmpty() && flatId == -1 -> {
                return dao.getAllTransactions(year = year)
            }
            transactionIndex == 0 && month.isEmpty() && flatId != -1 -> {
                return dao.getAllTransactionsByFlatId(year = year, flatId = flatId)
            }
            transactionIndex == 0 && month.isNotEmpty() && flatId == -1 -> {
                return dao.getAllTransactionsMonth(year = year, months = month)
            }
            transactionIndex == 0 && month.isNotEmpty() && flatId != -1 -> {
                return dao.getAllTransactionsByFlatIdM(year = year, flatId = flatId, months = month)
            }

            //Expenses
            transactionIndex == 1 && month.isEmpty() && flatId == -1 -> {
                return dao.getAllTransactions(year = year)
            }
            transactionIndex == 1 && month.isEmpty() && flatId != -1 -> {
                return dao.getAllTransactionsByFlatId(year = year, flatId = flatId)
            }
            transactionIndex == 1 && month.isNotEmpty() && flatId == -1 -> {
                return dao.getAllTransactionsMonth(year = year, months = month)
            }
            transactionIndex == 1 && month.isNotEmpty() && flatId != -1 -> {
                return dao.getAllTransactionsByFlatIdM(year = year, flatId = flatId, months = month)
            }
            else -> {
                throw IllegalArgumentException("Error")
            }
        }
    }
}