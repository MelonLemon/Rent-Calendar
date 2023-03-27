package com.melonlemon.rentcalendar.feature_transaction.domain.use_cases

import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class GetTransactions(
    private val repository: TransactionsRepository
) {
    operator fun invoke(transFilterState: TransFilterState): Flow<List<TransactionMonth>> {
//        if(transFilterState.year==0){
//            return emptyFlow()
//        }
//        if(transFilterState.chosenPeriod == TransactionPeriod.MonthsPeriod &&
//            transFilterState.chosenMonthsNum.isEmpty()){
//            return emptyFlow()
//        }
//
//        return repository.getTransactions(
//            transactionIndex =
//        )
        return flowOf()

    }
}