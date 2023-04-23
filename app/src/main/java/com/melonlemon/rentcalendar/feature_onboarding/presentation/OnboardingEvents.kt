package com.melonlemon.rentcalendar.feature_onboarding.presentation

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo

sealed class OnboardingEvents{
    data class OnSaveBaseOptionClick(
        val flats: List<String>,
        val monthlyExpCategories: List<ExpensesCategoryInfo>,
        val irregExpCategories: List<ExpensesCategoryInfo>
    ): OnboardingEvents()

    object RefreshErrorMessageState: OnboardingEvents()
}
