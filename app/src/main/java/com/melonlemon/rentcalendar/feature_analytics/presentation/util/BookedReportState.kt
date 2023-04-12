package com.melonlemon.rentcalendar.feature_analytics.presentation.util

data class BookedReportState(
    val averageBooked: Int = 0,
    val averageDayRent: Int = 0,
    val mostBookedMonth: Int = 1,
    val mostBookedMonthPercent: Int = 0,
    val mostIncomeMonth: Int = 0,
    val mostIncomeMonthAmount: Int = 0
)
