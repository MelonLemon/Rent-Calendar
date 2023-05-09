package com.melonlemon.rentcalendar.feature_onboarding.presentation.util

import androidx.annotation.StringRes

sealed class OnBoardingUiEvents {
    object FinishInitSettings: OnBoardingUiEvents()
    data class ShowErrorMessage(@StringRes val message: Int): OnBoardingUiEvents()
    object FinishOnBoarding: OnBoardingUiEvents()
}
