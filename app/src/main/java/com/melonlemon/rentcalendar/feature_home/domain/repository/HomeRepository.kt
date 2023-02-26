package com.melonlemon.rentcalendar.feature_home.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay

interface HomeRepository {

    suspend fun getFinResultsCurrentYear(flatId: Int): List<FinResultsDisplay>
    suspend fun getFinResultsAllFlatsCurrentYear(): List<FinResultsDisplay>
    suspend fun addNewFlat(name: String)
    suspend fun getAllFlats(): List<CategoryInfo>

}