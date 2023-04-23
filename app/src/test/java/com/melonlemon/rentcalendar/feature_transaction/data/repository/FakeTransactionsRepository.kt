package com.melonlemon.rentcalendar.feature_transaction.data.repository

import com.melonlemon.rentcalendar.core.domain.model.TransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class FakeTransactionsRepository: TransactionsRepository {

    val expensesTransactions = mutableMapOf<Int, List<TransactionsDay>>(
        3 to listOf<TransactionsDay>(
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,20),
                category = "Category 1",
                amount = -2000,
                comment = "3"
            ),
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,20),
                category = "Category 2",
                amount = -1000,
                comment = "2"
            )
        ),
        4 to listOf<TransactionsDay>(
            TransactionsDay(
                paymentDate = LocalDate.of(2023,4,15),
                category = "Category 1",
                amount = -2000,
                comment = "4"
            ),
            TransactionsDay(
                paymentDate = LocalDate.of(2023,4,18),
                category = "Category 2",
                amount = -1000,
                comment = "3"
            ),
            TransactionsDay(
                paymentDate = LocalDate.of(2023,4,18),
                category = "Category 2",
                amount = -1000,
                comment = "4"
            )
        )
    )
    val incomeTransactions = mutableMapOf<Int, List<TransactionsDay>>(
        3 to listOf<TransactionsDay>(
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,20),
                category = "",
                amount = 7000,
                comment = "7"
            ),
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,25),
                category = "",
                amount = 1000,
                comment = "1"
            )
        ),
        4 to listOf<TransactionsDay>(
            TransactionsDay(
                paymentDate = LocalDate.of(2023,4,1),
                category = "",
                amount = 18000,
                comment = "18"
            )
        )
    )
    val allTransactions = mutableMapOf<Int, List<TransactionsDay>>(
        3 to listOf<TransactionsDay>(
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,20),
                category = "",
                amount = 7000,
                comment = "7"
            ),
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,20),
                category = "Category 1",
                amount = -2000,
                comment = "3"
            ),
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,20),
                category = "Category 2",
                amount = -1000,
                comment = "2"
            ),
            TransactionsDay(
                paymentDate = LocalDate.of(2023,3,25),
                category = "",
                amount = 1000,
                comment = "1"
        ),

        ),
        4 to listOf<TransactionsDay>(
            TransactionsDay(
                paymentDate = LocalDate.of(2023,4,1),
                category = "",
                amount = 18000,
                comment = "18"
            )
        )
    )

    override fun getExpensesTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>> {
        return flow { emit(expensesTransactions) }
    }

    override fun getExpensesTransactionsByFlatId(
        flatId: List<Int>,
        year: Int
    ): Flow<Map<Int, List<TransactionsDay>>> {
        return flow { emit(expensesTransactions) }
    }

    override fun getIncomeTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>> {
        return flow { emit(incomeTransactions) }
    }

    override fun getIncomeTransactionsFlatId(
        flatId: List<Int>,
        year: Int
    ): Flow<Map<Int, List<TransactionsDay>>> {
        return flow { emit(incomeTransactions) }
    }

    override fun getAllTransactions(year: Int): Flow<Map<Int, List<TransactionsDay>>> {
        return flow { emit(allTransactions) }
    }

    override fun getAllTransactionsByFlatId(
        flatId: List<Int>,
        year: Int
    ): Flow<Map<Int, List<TransactionsDay>>> {
        return flow { emit(allTransactions) }
    }
}