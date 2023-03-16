package com.melonlemon.rentcalendar

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val icon: ImageVector){
    object HomeScreen: Screens("home_screen", Icons.Filled.Home)
    object AnalyticsScreen: Screens("analytics_screen", Icons.Filled.List)
    object TransactionsScreen: Screens("transactions_screen", Icons.Filled.List)
}
