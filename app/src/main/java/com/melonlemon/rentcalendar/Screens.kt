package com.melonlemon.rentcalendar

sealed class Screens(val route: String){
    object OnBoardingScreen: Screens("on_boarding_screen")
    object HomeScreen: Screens("home_screen")
    object AnalyticsScreen: Screens("analytics_screen")
    object TransactionsScreen: Screens("transactions_screen")
}
