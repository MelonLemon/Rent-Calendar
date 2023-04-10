package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.feature_analytics.domain.model.IncomeStatementInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.InvestmentReturnState

class GetIncomeStatement(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(flatId: Int): List<IncomeStatementInfo> {
        return emptyList()
    }
}