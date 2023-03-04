package com.melonlemon.rentcalendar.feature_home.domain.model

import java.time.LocalDate
import java.time.YearMonth


data class FinResultsDisplay(
    val flatId: Int,
    val flatName: String,
    val yearMonth: YearMonth,
    val income: Int,
    val expenses: Int,
    val percentBooked: Float
)

data class RentInfo(
    val id: Int,
    val name: String,
    val description: String,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val amount: Int,
    val isPaid: Boolean
)

data class ExpensesCategoryInfo(
    val id: Int,
    val header: String,
    val subHeader: String,
    val amount: Int
)