package com.melonlemon.rentcalendar.feature_home.presentation.util

sealed class CheckStatusNewFlat{
    object BlankFailStatus: CheckStatusNewFlat()
    object DuplicateFailStatus: CheckStatusNewFlat()
    object SuccessStatus: CheckStatusNewFlat()
    object UnKnownFailStatus: CheckStatusNewFlat()
    object UnCheckedStatus: CheckStatusNewFlat()
}
