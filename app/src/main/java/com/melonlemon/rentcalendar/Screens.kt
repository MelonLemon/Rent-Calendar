package com.melonlemon.rentcalendar

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

sealed class Screens(val route: String){
    object HomeScreen: Screens("home_screen")
    object AnalyticsScreen: Screens("analytics_screen")
    object TransactionsScreen: Screens("transactions_screen")
}
