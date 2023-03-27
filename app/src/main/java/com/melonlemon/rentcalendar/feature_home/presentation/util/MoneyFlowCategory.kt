package com.melonlemon.rentcalendar.feature_home.presentation.util

sealed class MoneyFlowCategory{
    object Regular: MoneyFlowCategory()
    object Irregular: MoneyFlowCategory()
}
