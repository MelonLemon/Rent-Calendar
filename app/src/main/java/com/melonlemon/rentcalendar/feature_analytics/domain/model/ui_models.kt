package com.melonlemon.rentcalendar.feature_analytics.domain.model

import androidx.compose.ui.graphics.Color
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo

data class IncomeStatementInfo(
    val quarter: Int,
    val netIncome: Int,
    val revenue: Int,
    val monthlyExp: Int,
    val irregularExp: Int
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
    fun barchartItem(index: Int):ChartItem{
        return ChartItem(name = name, color = color, value = values.getOrNull(index)?:0)
    }
}
data class CashFlowInfo(
    val quarter: Int,
    val netCashFlow: Int,
    val rent: Int,
    val expenses: List<DisplayInfo>
)