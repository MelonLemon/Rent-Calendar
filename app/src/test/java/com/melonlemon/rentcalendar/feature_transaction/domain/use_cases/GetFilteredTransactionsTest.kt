package com.melonlemon.rentcalendar.feature_transaction.domain.use_cases

import com.melonlemon.rentcalendar.feature_transaction.data.repository.FakeTransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.domain.model.AllTransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionListItem
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class GetFilteredTransactionsTest{
    private lateinit var getFilteredTransactions: GetFilteredTransactions

    @Before
    fun setUp(){

        getFilteredTransactions = GetFilteredTransactions()
    }

    @Test
    fun `Check filter mechanism`() = runBlocking{

        val transactionsBefore = listOf<TransactionMonth>(
            TransactionMonth(
                yearMonth = YearMonth.now(),
                amount =3500,
                currencySign = "$",
                daysList = listOf(
                    AllTransactionsDay(
                        date = LocalDate.now(),
                        transactions = listOf(
                            TransactionListItem(
                                id=1,
                                category = "cleaning",
                                comment = "4",
                                amount = -500,
                                currencySign = "$"
                            ),
                            TransactionListItem(
                                id=2,
                                category = "maintent",
                                comment = "4",
                                amount = -500,
                                currencySign = "$"
                            ),
                            TransactionListItem(
                                id=3,
                                category = "",
                                comment = "4",
                                amount = 4500,
                                currencySign = "$"
                            )
                        )
                    )
                )
            )
        )
        val transactionsAfter = listOf<TransactionMonth>(
            TransactionMonth(
                yearMonth = YearMonth.now(),
                amount =-500,
                currencySign = "$",
                daysList = listOf(
                    AllTransactionsDay(
                        date = LocalDate.now(),
                        transactions = listOf(
                            TransactionListItem(
                                id=1,
                                category = "cleaning",
                                comment = "4",
                                amount = -500,
                                currencySign = "$"
                            )
                        )
                    )
                )
            )
        )
        val transactions = getFilteredTransactions(searchText = "clea", transactionMonth = transactionsBefore)

        println("Result transactions: $transactions")
        assertEquals(transactionsAfter, transactions)

    }

}