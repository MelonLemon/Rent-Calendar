package com.melonlemon.rentcalendar.feature_onboarding.presentation


import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo

data class OnBoardingState(
    val newFlat: String = "",
    val tempFlats: List<String> = emptyList(),
    val isMonthCat: Boolean = true,
    val newNameCat: String = "",
    val newAmountCat: Int = 0,
    val tempMonthlyExpCat: List<DisplayInfo> = emptyList(),
    val tempIrregularExpCat: List<DisplayInfo> = emptyList(),
)
