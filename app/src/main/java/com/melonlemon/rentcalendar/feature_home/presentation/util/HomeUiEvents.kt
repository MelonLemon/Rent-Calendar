package com.melonlemon.rentcalendar.feature_home.presentation.util

import androidx.annotation.StringRes

sealed class HomeUiEvents{
    data class ShowMessage(@StringRes val message: Int): HomeUiEvents()
    object OpenCalendar: HomeUiEvents()
    object ScrollFinancialResults: HomeUiEvents()
}
