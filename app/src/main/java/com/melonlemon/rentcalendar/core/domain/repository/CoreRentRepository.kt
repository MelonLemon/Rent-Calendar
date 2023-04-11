package com.melonlemon.rentcalendar.core.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.Flats

interface CoreRentRepository {
    suspend fun getAllFlats(): List<Flats>
    suspend fun getAllYears(): List<Int>
}