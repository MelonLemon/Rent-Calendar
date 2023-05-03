package com.melonlemon.rentcalendar.feature_analytics.presentation.util

import com.melonlemon.rentcalendar.feature_analytics.domain.model.CashFlowInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.model.IncomeStatementInfo


data class AnalyticsDependState(
    val finSnapshotState: InvestmentReturnState = InvestmentReturnState(),
    val incomeStatementState: List<IncomeStatementInfo> = emptyList(),
    val cashFlowState: List<CashFlowInfo> = emptyList(),
    val bookedReportState: BookedReportState = BookedReportState()
)