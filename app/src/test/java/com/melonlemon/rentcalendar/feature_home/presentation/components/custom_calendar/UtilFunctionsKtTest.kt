package com.melonlemon.rentcalendar.feature_home.presentation.components.custom_calendar

import com.melonlemon.rentcalendar.feature_home.domain.model.SelectedWeekInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class UtilFunctionsKtTest{


    @Test
    fun `get selected Dates List Start period`() = runBlocking {

        val startDate = LocalDate.now()
        val endDate = null
        val selectionsCorrect = mapOf(
            17 to SelectedWeekInfo(index = 0, startDate = startDate, size = 1)
        )
        val selections = getSelectedDatesList(startDate = startDate, endDate = endDate)
        println("selections: $selections")
        assertEquals(selectionsCorrect, selections)
    }

    @Test
    fun `get selected Dates List one full period`() = runBlocking {

        val startDate = LocalDate.now()
        val endDate = LocalDate.now().plusDays(2)
        val selectionsCorrect = mapOf(
            17 to SelectedWeekInfo(index = 0, startDate = startDate, size = 3)
        )
        val selections = getSelectedDatesList(startDate = startDate, endDate = endDate)
        println("selections: $selections")
        assertEquals(selectionsCorrect, selections)
    }

    @Test
    fun `get selected Dates List 2 full period`() = runBlocking {

        val startDate = LocalDate.now()
        val endDate = LocalDate.now().plusDays(7)
        val selectionsCorrect = mapOf(
            17 to SelectedWeekInfo(index = 0, startDate = startDate, size = 4),
            18 to SelectedWeekInfo(index = 1, startDate = startDate.plusDays(4), size = 4)
        )
        val selections = getSelectedDatesList(startDate = startDate, endDate = endDate)
        println("selections: $selections")
        assertEquals(selectionsCorrect, selections)
    }
}