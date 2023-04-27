package com.melonlemon.rentcalendar.core.domain.repository

import com.melonlemon.rentcalendar.core.domain.model.Category
import com.melonlemon.rentcalendar.core.domain.model.CategoryType
import com.melonlemon.rentcalendar.core.domain.model.Flats

interface CoreRentRepository {
    suspend fun getAllFlats(): List<Flats>
    suspend fun getAllYears(): List<Int>
    suspend fun saveBaseOption(flats: List<Flats>, categories: List<Category>)
    suspend fun addCategoryType(categoryType: CategoryType)
}