package com.melonlemon.rentcalendar.feature_transaction.domain.use_cases

import com.melonlemon.rentcalendar.feature_transaction.domain.model.TransactionMonth
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class GetFilteredTransactions(

) {
    operator fun invoke(searchText: String, transactionsByMonth: List<TransactionMonth>): List<TransactionMonth> {
        if(searchText.isNotBlank()){
            return transactionsByMonth
        }  else {
            return transactionsByMonth
        }

    }
}