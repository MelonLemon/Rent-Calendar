package com.melonlemon.rentcalendar.feature_analytics.domain.model

import androidx.compose.ui.graphics.Color

data class IncomeStatementInfo(
    val quarter: Int,
    val netIncome: Int,
    val revenue: Int,
    val monthlyExp: Int,
    val irregExp: Int
)

data class DisplayInfo(
    val id: Int,
    val name: String,
    val amount: Int
)

data class ChartItem(
    val name: String,
    val color: Color,
    val value: Int
)

data class BarchartData(
    val name: String,
    val color: Color,
    val values: List<Int>
) {
    fun BarchartItem(index: Int):ChartItem{
        return ChartItem(name = name, color = color, value = values.getOrNull(index)?:0)
    }
}
data class CashFlowInfo(
    val quarter: Int,
    val netCashFlow: Int,
    val rent: Int,
    val expenses: List<DisplayInfo>
)