package com.melonlemon.rentcalendar.feature_home.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusNewFlat

data class FlatsState(
    val newFlat: String = "",
    val listOfFlats: List<CategoryInfo> = emptyList(),
    val checkStatusNewFlat: CheckStatusNewFlat = CheckStatusNewFlat.UnCheckedStatus
)
