package com.melonlemon.rentcalendar.feature_transaction.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import java.time.YearMonth

data class TransFilterState(
    val transactionType: TransactionType = TransactionType.AllTransaction,
    val flats: List<CategoryInfo> = emptyList(),
    val selectedFlatsId: List<Int> = emptyList(),
    val chosenPeriod: TransactionPeriod = TransactionPeriod.YearPeriod,
    val year: Int = YearMonth.now().year,
    val chosenMonthsNum: List<Int> = listOf(1)
)
