package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.SchedulePageState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class GetRentList(
    private val repository: HomeRepository
) {
     @OptIn(ExperimentalCoroutinesApi::class)
     operator fun invoke(yearMonth: YearMonth, flatId: Int): Flow<List<RentInfo>> {

         val rents = repository.getRentList(
             year = yearMonth.year,
             month = yearMonth.monthValue,
             flatId = flatId
         )
        return rents.mapLatest { rentsList ->
            rentsList.map {  rent ->
                RentInfo(
                    id = rent.schedule.id!!,
                    name = rent.person.name,
                    description = rent.schedule.comment,
                    periodStart = rent.schedule.startDate,
                    periodEnd = rent.schedule.endDate,
                    amount = rent.payment.paymentAllNights,
                    isPaid = rent.payment.isPaid
                )
            }

        }
    }
}