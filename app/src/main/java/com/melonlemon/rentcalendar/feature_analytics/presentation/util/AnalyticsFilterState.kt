package com.melonlemon.rentcalendar.feature_analytics.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import java.time.YearMonth

data class AnalyticsFilterState(
    val selectedYearId: Int = -1,
    val selectedFlatId: Int = -1,
    val chosenYear: Int = YearMonth.now().year
)
