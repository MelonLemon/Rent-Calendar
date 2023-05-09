package com.melonlemon.rentcalendar.feature_home.domain.use_cases


import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.presentation.util.SchedulePageInfo
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GetSchedulePageInfo {
     operator fun invoke(rentList: List<RentInfo>): SchedulePageInfo {
        if(rentList.isNotEmpty()){
            val currentYear = rentList[0].periodStart.year
            val currentMonth = rentList[0].periodStart.month
            var startDate = LocalDate.of(currentYear, currentMonth, 1)
            val vacantList = mutableListOf<Pair<LocalDate, LocalDate>>()
            var vacantDays = 0
            var amountPaid = 0
            var amountForecast = 0
            rentList.forEach { rentInfo ->
                if(startDate.isBefore(rentInfo.periodStart)) {
                    vacantList.add(Pair(startDate, rentInfo.periodStart))
                    vacantDays += ChronoUnit.DAYS.between(startDate, rentInfo.periodStart).toInt()
                }
                startDate = rentInfo.periodEnd
                if(rentInfo.isPaid){
                    amountPaid+= rentInfo.amount
                } else {
                    amountForecast+= rentInfo.amount
                }

            }
            return SchedulePageInfo(
                vacantDays = vacantDays,
                vacantList = vacantList,
                amountPaid = amountPaid,
                amountForecast = amountForecast

            )
        } else {
            return SchedulePageInfo()
        }

    }
}