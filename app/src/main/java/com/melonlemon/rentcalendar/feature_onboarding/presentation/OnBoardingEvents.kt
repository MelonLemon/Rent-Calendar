package com.melonlemon.rentcalendar.feature_onboarding.presentation

import androidx.annotation.StringRes

sealed class OnBoardingEvents{
    object OnSaveBaseOptionClick: OnBoardingEvents()
    data class SendMessage(@StringRes val message: Int): OnBoardingEvents()
    //FLAT PAGE
    data class OnNewNameChanged(val name:String): OnBoardingEvents()
    object OnNewFlatAdd: OnBoardingEvents()
    data class OnNameFlatChanged(val index: Int, val name:String): OnBoardingEvents()
    data class OnSegmentBtnClick(val isMonthCatChosen: Boolean): OnBoardingEvents()
    data class OnNewAmountChange(val amount: Int): OnBoardingEvents()
    data class OnNewNameChange(val name: String): OnBoardingEvents()
    object OnNewCatAdd: OnBoardingEvents()
    data class OnNameCatChanged(val index: Int, val name:String): OnBoardingEvents()
    data class OnAmountCatChanged(val index: Int, val amount:Int): OnBoardingEvents()


}
