package com.melonlemon.rentcalendar.core.presentation.util

import androidx.annotation.StringRes
import com.melonlemon.rentcalendar.R

data class SendMessage(
    val send: Boolean = false,
    @StringRes val message: Int = R.string.err_msg_base_error
)
