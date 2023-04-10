package com.melonlemon.rentcalendar.core.data.repository

import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.domain.model.Flats
import com.melonlemon.rentcalendar.core.domain.repository.CoreRentRepository

class CoreRentRepositoryImpl(
    private val dao: RentDao
): CoreRentRepository {
    override suspend fun getAllFlats(): List<Flats> {
        return dao.getFlats()
    }
}