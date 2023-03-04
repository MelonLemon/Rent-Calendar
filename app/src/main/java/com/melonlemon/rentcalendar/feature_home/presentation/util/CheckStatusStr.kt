package com.melonlemon.rentcalendar.feature_home.presentation.util

sealed class CheckStatusStr{
    object BlankFailStatus: CheckStatusStr()
    object DuplicateFailStatus: CheckStatusStr()
    object SuccessStatus: CheckStatusStr()
    object UnKnownFailStatus: CheckStatusStr()
    object UnCheckedStatus: CheckStatusStr()
}
