package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository

class GetAllFlats(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): List<CategoryInfo>{
        val flats = repository.getAllFlats().filter { it.active }
        return flats.map { CategoryInfo(
            id = it.id!!,
            name = it.name
        ) }
    }
}