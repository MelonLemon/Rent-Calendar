package com.melonlemon.rentcalendar.feature_home.presentation.util

sealed class HomeScreenEvents{
    //New Flat
    data class OnNewFlatChanged(val name: String): HomeScreenEvents()
    object OnAddNewFlatBtnClick: HomeScreenEvents()
    object OnAddNewFlatComplete: HomeScreenEvents()
    data class OnFlatClick(val id: Int): HomeScreenEvents()
    //Home Screen State
    data class OnPageChange(val page: HomePages): HomeScreenEvents()

}
