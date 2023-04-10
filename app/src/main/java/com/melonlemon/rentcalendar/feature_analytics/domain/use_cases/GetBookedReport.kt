package com.melonlemon.rentcalendar.feature_analytics.domain.use_cases

import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.BookedReportState

class GetBookedReport(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(flatId: Int): BookedReportState {
        return BookedReportState()
    }
}