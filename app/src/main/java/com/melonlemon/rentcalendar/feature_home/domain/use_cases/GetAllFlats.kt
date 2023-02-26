package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository

class GetAllFlats(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): List<CategoryInfo>{
        return repository.getAllFlats()
    }
}