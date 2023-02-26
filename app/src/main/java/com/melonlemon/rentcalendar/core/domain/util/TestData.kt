package com.melonlemon.rentcalendar.core.domain.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import java.time.YearMonth

class TestData {

    @RequiresApi(Build.VERSION_CODES.O)
    val finResults = mapOf(
        1 to listOf<FinResultsDisplay>(
            FinResultsDisplay(
            flatId = 1,
            flatName = "Central Flat",
            yearMonth = YearMonth.now().minusMonths(2),
            income = 50000,
            expenses = 17000,
            percentBooked = 0.6f
        ),
            FinResultsDisplay(
                flatId = 1,
                flatName = "Central Flat",
                yearMonth = YearMonth.now().minusMonths(1),
                income = 60000,
                expenses = 15000,
                percentBooked = 0.7f
            ),
            FinResultsDisplay(
                flatId = 1,
                flatName = "Central Flat",
                yearMonth = YearMonth.now(),
                income = 50000,
                expenses = 17000,
                percentBooked = 0.6f
            ),
            FinResultsDisplay(
                flatId = 1,
                flatName = "Central Flat",
                yearMonth = YearMonth.now().plusMonths(1),
                income = 10000,
                expenses = 0,
                percentBooked = 0.2f
            ),
        ),
        2 to listOf<FinResultsDisplay>(
            FinResultsDisplay(
                flatId = 2,
                flatName = "Secondary Flat",
                yearMonth = YearMonth.now().minusMonths(2),
                income = 50000,
                expenses = 17000,
                percentBooked = 0.6f
            ),
            FinResultsDisplay(
                flatId = 2,
                flatName = "Secondary Flat",
                yearMonth = YearMonth.now().minusMonths(1),
                income = 60000,
                expenses = 15000,
                percentBooked = 0.7f
            ),
            FinResultsDisplay(
                flatId = 2,
                flatName = "Secondary Flat",
                yearMonth = YearMonth.now(),
                income = 50000,
                expenses = 17000,
                percentBooked = 0.6f
            ),
            FinResultsDisplay(
                flatId = 2,
                flatName = "Secondary Flat",
                yearMonth = YearMonth.now().plusMonths(2),
                income = 10000,
                expenses = 0,
                percentBooked = 0.2f
            ),
        ),
    )
    @RequiresApi(Build.VERSION_CODES.O)
    val finResultsAllFlats = listOf<FinResultsDisplay>(
        FinResultsDisplay(
            flatId = -1,
            flatName = "All",
            yearMonth = YearMonth.now().minusMonths(2),
            income = 100000,
            expenses = 34000,
            percentBooked = 0.6f
        ),
        FinResultsDisplay(
            flatId = -1,
            flatName = "All",
            yearMonth = YearMonth.now().minusMonths(1),
            income = 1200000,
            expenses = 30000,
            percentBooked = 0.7f
        ),
        FinResultsDisplay(
            flatId = -1,
            flatName = "All",
            yearMonth = YearMonth.now(),
            income = 100000,
            expenses = 34000,
            percentBooked = 0.6f
        ),
        FinResultsDisplay(
            flatId = -1,
            flatName = "All",
            yearMonth = YearMonth.now().plusMonths(1),
            income = 10000,
            expenses = 0,
            percentBooked = 0.2f
        ),
        FinResultsDisplay(
            flatId = -1,
            flatName = "All",
            yearMonth = YearMonth.now().plusMonths(2),
            income = 10000,
            expenses = 0,
            percentBooked = 0.2f
        ),
    )

    val allflats = listOf<CategoryInfo>(
        CategoryInfo(1, "Central Flat"),
        CategoryInfo(2, "Secondary Flat")
    )
}