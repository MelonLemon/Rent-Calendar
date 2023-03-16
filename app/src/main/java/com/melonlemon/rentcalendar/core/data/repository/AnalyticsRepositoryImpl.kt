package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository

class AnalyticsRepositoryImpl(
    private val dao: RentDao
): AnalyticsRepository {
}