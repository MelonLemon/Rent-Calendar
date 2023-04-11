package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.model.CashFlowInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository

class GetCashFlowInfo(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(year: Int, flatId: Int): List<CashFlowInfo> {
        if(flatId==-1){

        } else {

        }
        return emptyList()
    }
}