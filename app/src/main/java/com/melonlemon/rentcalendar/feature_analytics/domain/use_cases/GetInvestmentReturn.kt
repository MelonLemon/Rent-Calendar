package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.InvestmentReturnState

class GetInvestmentReturn(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(year: Int, flatId: Int): InvestmentReturnState {
        if(flatId==-1){

        } else {

        }
        return InvestmentReturnState()
    }
}