package com.melonlemon.rentcalendar.feature_transaction.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.TransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.model.AllTransactionsDay
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionListItem
import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionPeriod
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.YearMonth

class GetTransactions(
    private val repository: TransactionsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        flatIds: List<Int>,
        year: Int,
        transactionType: TransactionType,
        currencySign: String,
        searchText: String?=null): Flow<List<TransactionMonth>> {
        if(year==0){
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
                val filtredDays: List<TransactionsDay>
                if(searchText!=null && searchText.isNotBlank()){
                    filtredDays = info.second.filter { it.category.contains(searchText) || it.comment.contains(searchText) }
                } else {
                    filtredDays = info.second
                }
                val daysList = filtredDays.groupBy { it.paymentDate }.toList().map{ dayList ->
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

                val allAmount = info.second.sumOf { it.amount }
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