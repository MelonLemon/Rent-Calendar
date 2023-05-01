package com.melonlemon.rentcalendar.feature_onboarding.presentation

import androidx.annotation.StringRes

sealed class OnBoardingUiEvents {
    data class ShowErrorMessage(@StringRes val message: Int): OnBoardingUiEvents()
    object FinishOnBoarding: OnBoardingUiEvents()
}
