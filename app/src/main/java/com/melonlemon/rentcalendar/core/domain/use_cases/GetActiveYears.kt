package com.melonlemon.rentcalendar.core.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.repository.CoreRentRepository
import java.time.LocalDate

class GetActiveYears(
    private val repository: CoreRentRepository
) {
    suspend operator fun invoke(): List<CategoryInfo>{
        val years = repository.getAllYears()
        val newYears = years.ifEmpty { listOf(CategoryInfo(id=0, LocalDate.now().year.toString())) }
        return newYears.mapIndexed { index, year ->
            CategoryInfo(
            id = index,
            name = year.toString()
        ) }
    }
}