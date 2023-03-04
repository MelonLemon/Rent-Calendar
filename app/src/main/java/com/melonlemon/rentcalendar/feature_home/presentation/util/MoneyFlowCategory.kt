package com.melonlemon.rentcalendar.feature_home.presentation.util

sealed class MoneyFlowCategory{
    object RegularFixed: MoneyFlowCategory()
    object IrregularFixed: MoneyFlowCategory()
    object RegularVariable: MoneyFlowCategory()
    object IrregularVariable: MoneyFlowCategory()
}
