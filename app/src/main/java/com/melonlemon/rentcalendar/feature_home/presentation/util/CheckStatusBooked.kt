package com.melonlemon.rentcalendar.feature_home.presentation.util

sealed class CheckStatusBooked{
    object BlankDatesFailStatus: CheckStatusBooked()
    object BlankNameFailStatus: CheckStatusBooked()
    object BlankPaymentFailStatus: CheckStatusBooked()
    object SuccessStatus: CheckStatusBooked()
    object UnKnownFailStatus: CheckStatusBooked()
    object UnCheckedStatus: CheckStatusBooked()
}


