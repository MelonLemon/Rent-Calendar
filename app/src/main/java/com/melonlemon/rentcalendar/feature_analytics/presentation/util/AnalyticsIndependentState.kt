package com.melonlemon.rentcalendar.feature_analytics.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import java.time.YearMonth

data class AnalyticsIndependentState(
    val chosenReport: Reports = Reports.IncomeStatement,
    val listOfFlats: List<CategoryInfo> = emptyList(),
    val listOfYears: List<CategoryInfo> = listOf(
        CategoryInfo(id=0, name = YearMonth.now().year.toString())
    ),
    val totalPurchasePrice: Int = 0,
    val currencySign: String = ""

)
