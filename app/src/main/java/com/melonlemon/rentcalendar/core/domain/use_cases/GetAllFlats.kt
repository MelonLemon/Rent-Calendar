package com.melonlemon.rentcalendar.core.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.repository.CoreRentRepository

class GetAllFlats(
    private val repository: CoreRentRepository
) {
    suspend operator fun invoke(): List<CategoryInfo>{
        val flats = repository.getAllFlats().filter { it.active }
        return flats.map { CategoryInfo(
            id = it.id!!,
            name = it.name
        ) }
    }
}