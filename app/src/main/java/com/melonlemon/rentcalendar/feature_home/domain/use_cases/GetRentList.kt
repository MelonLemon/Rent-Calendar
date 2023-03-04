package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.SchedulePageState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class GetRentList(
    private val repository: HomeRepository
) {
     operator fun invoke(yearMonth: YearMonth, flatId: Int): Flow<List<RentInfo>> {

        return repository.getRentList(yearMonth=yearMonth, flatId=flatId)
    }
}