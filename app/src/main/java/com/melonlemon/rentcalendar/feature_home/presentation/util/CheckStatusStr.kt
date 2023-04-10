package com.melonlemon.rentcalendar.feature_home.presentation.util

import androidx.annotation.StringRes
import com.melonlemon.rentcalendar.R

sealed class CheckStatusStr(@StringRes val message: Int){
    object BlankFailStatus: CheckStatusStr(R.string.err_msg_empty)
    object DuplicateFailStatus: CheckStatusStr(R.string.err_msg_duplicate_name)
    object SuccessStatus: CheckStatusStr(R.string.msg_success_status)
    object UnKnownFailStatus: CheckStatusStr(R.string.err_msg_unknown_error)
    object UnCheckedStatus: CheckStatusStr(R.string.unchecked_status)
}
