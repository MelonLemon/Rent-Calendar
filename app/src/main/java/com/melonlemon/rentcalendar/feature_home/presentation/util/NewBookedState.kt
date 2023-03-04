package com.melonlemon.rentcalendar.feature_home.presentation.util

import java.time.LocalDate

data class NewBookedState(
    val startDate: LocalDate?=null,
    val endDate: LocalDate?=null,
    val name: String="",
    val comment: String="",
    val nights: Int=0,
    val oneNightMoney: Int=0,
    val allMoney: Int=0,
    val checkStatusNewBooked: CheckStatusBooked = CheckStatusBooked.UnCheckedStatus
)
