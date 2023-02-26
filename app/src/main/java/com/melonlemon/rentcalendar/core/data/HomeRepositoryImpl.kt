package com.melonlemon.rentcalendar.core.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.util.TestData
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository

class HomeRepositoryImpl: HomeRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFinResultsCurrentYear(flatId: Int): List<FinResultsDisplay> {
        val testData = TestData()
        return testData.finResults[flatId]?: emptyList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getFinResultsAllFlatsCurrentYear(): List<FinResultsDisplay> {
        val testData = TestData()
        return testData.finResultsAllFlats
    }

    override suspend fun addNewFlat(name: String) {

    }

    override suspend fun getAllFlats(): List<CategoryInfo> {
        val testData = TestData()
        return testData.allflats
    }
}