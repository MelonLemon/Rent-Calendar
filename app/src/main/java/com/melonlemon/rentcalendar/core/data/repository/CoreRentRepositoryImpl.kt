package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.domain.model.Category
import com.melonlemon.rentcalendar.core.domain.model.Flats
import com.melonlemon.rentcalendar.core.domain.repository.CoreRentRepository

class CoreRentRepositoryImpl(
    private val dao: RentDao
): CoreRentRepository {
    override suspend fun getAllFlats(): List<Flats> {
        return dao.getFlats()
    }

    override suspend fun getAllYears(): List<Int> {
        return dao.getYearsActive()
    }

    override suspend fun saveBaseOption(flats: List<Flats>, categories: List<Category>) {
        dao.saveBaseOption(flats=flats,categories=categories)
    }
}