package com.melonlemon.rentcalendar.feature_home.presentation.util

import androidx.annotation.StringRes
import com.melonlemon.rentcalendar.R

sealed class CheckStatusBooked(@StringRes val message: Int){
    object BlankDatesFailStatus: CheckStatusBooked(R.string.booked_err_msg_dates)
    object BlankNameFailStatus: CheckStatusBooked(R.string.booked_err_msg_name)
    object BlankPaymentFailStatus: CheckStatusBooked(R.string.booked_err_msg_pay)
    object SuccessStatus: CheckStatusBooked(R.string.msg_success_status)
    object UnKnownFailStatus: CheckStatusBooked(R.string.err_msg_unknown_error)
    object UnCheckedStatus: CheckStatusBooked(R.string.unchecked_status)
}


