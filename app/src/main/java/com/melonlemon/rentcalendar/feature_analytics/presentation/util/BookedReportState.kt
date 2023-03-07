package com.melonlemon.rentcalendar.feature_analytics.presentation.util

data class BookedReportState(
    val averageBooked: Int = 0,
    val averageDayRent: Int = 0,
    val bestBookedDays: String = "",
    val bestMonth: String = "",
    val bestMonthBooked: Int = 0,
    val bestMonthIncome: Int = 0
)
