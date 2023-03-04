package com.melonlemon.rentcalendar.feature_home.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.YearMonth


data class HomeScreenState(
    val page: HomePages = HomePages.SchedulePage
)
