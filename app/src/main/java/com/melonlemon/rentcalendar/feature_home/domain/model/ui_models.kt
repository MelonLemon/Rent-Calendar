package com.melonlemon.rentcalendar.feature_home.domain.model

import java.time.YearMonth


data class FinResultsDisplay(
    val flatId: Int,
    val flatName: String,
    val yearMonth: YearMonth,
    val income: Int,
    val expenses: Int,
    val percentBooked: Float
)