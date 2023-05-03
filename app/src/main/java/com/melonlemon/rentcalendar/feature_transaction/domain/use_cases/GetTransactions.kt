package com.melonlemon.rentcalendar.feature_transaction.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.TransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.model.AllTransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionListItem
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.YearMonth

class GetTransactions(
    private val repository: TransactionsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        transFilterInit: Boolean,
        flatIds: List<Int>,
        year: Int,
        transactionType: TransactionType,
        currencySign: String): Flow<List<TransactionMonth>> {

        if(!transFilterInit){
            return flowOf()
        }

        var transactions: Flow<Map<Int, List<TransactionsDay>>> = flowOf()
        val isAllFlatChose = flatIds.contains(-1)

        when(transactionType to isAllFlatChose){
            TransactionType.IncomeTransaction to true -> {
                transactions = repository.getIncomeTransactions(year=year)
            }
            TransactionType.IncomeTransaction to false -> {
                transactions = repository.getIncomeTransactionsFlatId(year=year, flatId = flatIds)
            }
            TransactionType.ExpensesTransaction to true -> {
                transactions = repository.getExpensesTransactions(year=year)
            }
            TransactionType.ExpensesTransaction to false -> {
                transactions = repository.getExpensesTransactionsByFlatId(year=year, flatId = flatIds)
            }
            TransactionType.AllTransaction to true -> {
                transactions = repository.getAllTransactions(year=year)
            }
            TransactionType.AllTransaction to false -> {
                transactions = repository.getAllTransactionsByFlatId(year=year, flatId = flatIds)
            }
        }

        val result = transactions.mapLatest {

            it.toList().map { info ->

                val filteredDays: List<TransactionsDay> = info.second
                val daysList = filteredDays.groupBy { days -> days.paymentDate }.toList().map{ dayList ->
                    AllTransactionsDay(
                        date = dayList.first,
                        transactions = dayList.second.mapIndexed { index, day ->
                            TransactionListItem(
                                id = index,
                                category = day.category,
                                comment = day.comment,
                                amount = day.amount,
                                currencySign = currencySign
                            )
                        }
                    )
                }

                val allAmount = info.second.sumOf { days -> days.amount }
                TransactionMonth(
                    yearMonth = YearMonth.of(year, info.first),
                    amount = allAmount,
                    currencySign = currencySign,
                    daysList = daysList
                )
        } }

        return result

    }
}