package com.melonlemon.rentcalendar.feature_home.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
data class HomeScreenState(
    val page: HomePages = HomePages.SchedulePage,
    val yearMonth: YearMonth = YearMonth.now()
)
