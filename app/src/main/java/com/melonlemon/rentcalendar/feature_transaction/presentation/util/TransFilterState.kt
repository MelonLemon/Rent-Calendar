package com.melonlemon.rentcalendar.feature_transaction.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo

data class TransFilterState(
    val transactionType: TransactionType = TransactionType.AllTransaction,
    val flats: List<CategoryInfo> = emptyList(),
    val selectedFlatsId: List<Int> = emptyList(),
    val chosenPeriod: TransactionPeriod = TransactionPeriod.YearPeriod,
    val years: List<CategoryInfo> = emptyList(),
    val selectedYearId: Int = -1,
    val chosenMonthsNum: List<Int> = listOf(1)
)
