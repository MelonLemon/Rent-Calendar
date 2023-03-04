package com.melonlemon.rentcalendar.core.presentation.util

sealed class SimpleStatusOperation{
    object OperationSuccess: SimpleStatusOperation()
    object OperationFail: SimpleStatusOperation()
}


