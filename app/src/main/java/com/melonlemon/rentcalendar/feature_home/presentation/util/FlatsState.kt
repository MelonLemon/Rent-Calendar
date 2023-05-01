package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo

data class FlatsState(
    val newFlat: String = "",
    val listOfFlats: List<CategoryInfo> = emptyList()
)
