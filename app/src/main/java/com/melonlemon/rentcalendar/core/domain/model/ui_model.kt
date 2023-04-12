package com.melonlemon.rentcalendar.core.domain.model

import java.time.YearMonth

data class CategoryInfo(
    val id: Int,
    val name: String
)

data class DisplayInfo(
    val name: String,
    val amount: Int
)