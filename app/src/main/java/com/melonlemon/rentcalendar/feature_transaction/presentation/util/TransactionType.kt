package com.melonlemon.rentcalendar.feature_transaction.presentation.util

import androidx.annotation.StringRes
import com.melonlemon.rentcalendar.R

sealed class TransactionType(@StringRes val name: Int){
    object ExpensesTransaction: TransactionType(R.string.expenses)
    object IncomeTransaction: TransactionType(R.string.income)
    object AllTransaction: TransactionType(R.string.all)
}
