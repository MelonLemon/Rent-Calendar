package com.melonlemon.rentcalendar.feature_transaction.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import java.time.YearMonth

data class TransFilterState(
    val transactionType: TransactionType = TransactionType.AllTransaction,
    val flats: List<CategoryInfo> = emptyList(),
    val selectedFlatsId: List<Int> = listOf(-1),
    val chosenPeriod: TransactionPeriod = TransactionPeriod.YearPeriod,
    val years: List<CategoryInfo> = listOf(CategoryInfo(id=0, name=YearMonth.now().year.toString())),
    val selectedYearId: Int = 0,
    val chosenMonthsNum: List<Int> = listOf(1),
    val transFilterInit: Boolean = false,
    val currency: String = ""
)
