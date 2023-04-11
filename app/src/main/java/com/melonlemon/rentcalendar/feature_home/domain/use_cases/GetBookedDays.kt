package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import java.time.LocalDate

class GetBookedDays(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(year: Int, flatId: Int): Map<Int, List<LocalDate>>?{
        return repository.getBookedDaysByWeek(year=year, flatId=flatId)
    }
}