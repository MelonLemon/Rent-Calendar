package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

data class AnalyticsUseCases(
    val getCashFlowInfo: GetCashFlowInfo,
    val getInvestmentReturn: GetInvestmentReturn,
    val getIncomeStatement: GetIncomeStatement,
    val getBookedReport: GetBookedReport
)
