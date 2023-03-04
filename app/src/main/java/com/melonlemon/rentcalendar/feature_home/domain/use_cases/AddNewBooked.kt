package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusBooked
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusStr
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory
import com.melonlemon.rentcalendar.feature_home.presentation.util.NewBookedState

class AddNewBooked(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(newBookedState: NewBookedState): CheckStatusBooked {
        if(newBookedState.startDate==null || newBookedState.endDate==null){
            return CheckStatusBooked.BlankDatesFailStatus
        }
        if(newBookedState.name.isBlank()){
            return CheckStatusBooked.BlankNameFailStatus
        }
        if(newBookedState.allMoney==0){
            return CheckStatusBooked.BlankPaymentFailStatus
        }
        try {
            repository.addNewRent(newBookedState)
        } catch (e: Exception){
            return CheckStatusBooked.UnKnownFailStatus
        }
        return CheckStatusBooked.SuccessStatus
    }
}