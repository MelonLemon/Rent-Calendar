package com.melonlemon.rentcalendar.feature_analytics.presentation.util

sealed class AnalyticsScreenEvents{
    data class OnReportChange(val report: Reports): AnalyticsScreenEvents()
    data class OnFlatClick(val id: Int): AnalyticsScreenEvents()
    data class OnYearClick(val id: Int): AnalyticsScreenEvents()
    data class OnTotalPurchaseChange(val price: Int): AnalyticsScreenEvents()
}
