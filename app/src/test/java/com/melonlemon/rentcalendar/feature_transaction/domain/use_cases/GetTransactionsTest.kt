package com.melonlemon.rentcalendar.feature_transaction.domain.use_cases

import com.melonlemon.rentcalendar.core.data.repository.TransactionRepositoryImpl
import com.melonlemon.rentcalendar.feature_transaction.data.repository.FakeTransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.domain.model.AllTransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionListItem
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class GetTransactionsTest{

    private lateinit var getTransactions: GetTransactions
    private lateinit var fakeTransactionsRepository: FakeTransactionsRepository

    @Before
    fun setUp(){
        fakeTransactionsRepository = FakeTransactionsRepository()
        getTransactions = GetTransactions(fakeTransactionsRepository)
    }

    //PASSED
    @Test
    fun `Transform Map Month to TransactionsDay to List TransactionMonth`() = runBlocking{

        val transactions = getTransactions(
            transactionType = TransactionType.AllTransaction,
            flatIds = listOf(-1),
            year=2023, currencySign = "$").first()

        val listOfMonths = listOf(
            TransactionMonth(
                yearMonth = YearMonth.of(2023,3),
                amount = 5000,
                currencySign = "$",
                daysList = listOf(
                    AllTransactionsDay(
                        date = LocalDate.of(2023,3,20),
                        transactions = listOf(
                            TransactionListItem(
                                id=0,
                                category = "",
                                comment = "7",
                                amount = 7000,
                                currencySign = "$"),
                            TransactionListItem(
                                id=1,
                                category = "Category 1",
                                comment = "3",
                                amount = -2000,
                                currencySign = "$"),
                            TransactionListItem(
                                id=2,
                                category = "Category 2",
                                comment = "2",
                                amount = -1000,
                                currencySign = "$"),

                        )
                    ),
                    AllTransactionsDay(
                        date = LocalDate.of(2023,3,25),
                        transactions = listOf(
                            TransactionListItem(
                                id=0,
                                category = "",
                                comment = "1",
                                amount = 1000,
                                currencySign = "$"),
                        )
                    )
                )
            ),
            TransactionMonth(
                yearMonth = YearMonth.of(2023,4),
                amount = 18000,
                currencySign = "$",
                daysList = listOf(
                    AllTransactionsDay(
                        date = LocalDate.of(2023,4,1),
                        transactions = listOf(
                            TransactionListItem(
                                id=0,
                                category = "",
                                comment = "18",
                                amount = 18000,
                                currencySign = "$")
                    )
                )
            )
        ))

        println("Result transactions: $transactions")
        assertEquals(listOfMonths, transactions)
    }

}