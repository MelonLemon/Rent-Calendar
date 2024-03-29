package com.melonlemon.rentcalendar.core.domain.use_cases

data class CoreRentUseCases(
    val getAllFlats: GetAllFlats,
    val getActiveYears: GetActiveYears,
    val saveBaseOption: SaveBaseOption
)
