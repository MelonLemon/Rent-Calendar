package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.Payment
import com.melonlemon.rentcalendar.core.domain.model.Person
import com.melonlemon.rentcalendar.core.domain.model.Schedule
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusBooked
import com.melonlemon.rentcalendar.feature_home.presentation.util.NewBookedState
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class AddNewBooked(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(newBookedState: NewBookedState, flatId: Int): CheckStatusBooked {
        if(newBookedState.startDate==null || newBookedState.endDate==null){
            return CheckStatusBooked.BlankDatesFailStatus
        }
        if(newBookedState.name.isBlank()){
            return CheckStatusBooked.BlankNameFailStatus
        }
        if(newBookedState.allMoney==0){
            return CheckStatusBooked.BlankPaymentFailStatus
        }
        if(newBookedState.startDate.isAfter(newBookedState.endDate)){

            return CheckStatusBooked.UnKnownFailStatus
        }
        try {

            val person = Person(id = null, name = newBookedState.name, phone = newBookedState.phone)
            val yearMonthStartDate = YearMonth.from(newBookedState.startDate)
            val yearMonthEndDate = YearMonth.from(newBookedState.endDate)
            if(yearMonthStartDate!=yearMonthEndDate){

                var temptEndDate = yearMonthStartDate.atEndOfMonth()
                var temptStartDate = newBookedState.startDate
                val paymentsList = mutableListOf<Payment>()
                val scheduleList = mutableListOf<Schedule>()
                while(true){
                    println("Start loop")
                    val nights = ChronoUnit.DAYS.between(temptStartDate, temptEndDate).toInt()
                    paymentsList.add(
                        Payment(
                            id=null,
                            flatId = flatId,
                            year = temptStartDate!!.year,
                            month = temptStartDate.monthValue,
                            nights = nights,
                            paymentSingleNight = newBookedState.oneNightMoney,
                            paymentAllNights = newBookedState.oneNightMoney*nights,
                            paymentDate = LocalDate.now(),
                            isPaid = newBookedState.isPaid
                        )
                    )
                    scheduleList.add(
                        Schedule(
                            id = null,
                            flatId = flatId,
                            year = temptStartDate.year,
                            month = temptStartDate.monthValue,
                            startDate = temptStartDate,
                            endDate = temptStartDate,
                            personId = -1,
                            paymentId = -1,
                            comment = newBookedState.comment
                        )
                    )
                    if(temptEndDate.isEqual(newBookedState.endDate)) break
                    val nextMonth = YearMonth.from(temptStartDate).plusMonths(1)
                    temptStartDate = nextMonth.atDay(1)
                    temptEndDate = if(nextMonth == yearMonthEndDate) newBookedState.endDate else
                        nextMonth.plusMonths(1).atDay(1)
                }

                repository.addNewRent(
                    person = person,
                    payments = paymentsList,
                    schedules = scheduleList
                )


            } else {

                val payments = listOf(
                    Payment(
                        id=null,
                        flatId = flatId,
                        year = yearMonthStartDate.year,
                        month = yearMonthStartDate.monthValue,
                        nights = newBookedState.nights,
                        paymentSingleNight = newBookedState.oneNightMoney,
                        paymentAllNights = newBookedState.allMoney,
                        paymentDate = LocalDate.now(),
                        isPaid = newBookedState.isPaid
                    )
                )
                val schedules = listOf(
                    Schedule(
                        id = null,
                        flatId = flatId,
                        year = yearMonthStartDate.year,
                        month = yearMonthStartDate.monthValue,
                        startDate = newBookedState.startDate,
                        endDate = newBookedState.endDate,
                        personId = -1,
                        paymentId = -1,
                        comment = newBookedState.comment
                    )
                )
                repository.addNewRent(
                    person = person,
                    payments = payments,
                    schedules = schedules
                )


            }

        } catch (e: Exception){

            return CheckStatusBooked.UnKnownFailStatus
        }
        return CheckStatusBooked.SuccessStatus
    }
}