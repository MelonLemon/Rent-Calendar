package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusStr
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory

class AddNewExpCat(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(
        name: String,
        amount: Int,
        categories: List<ExpensesCategoryInfo>,
        moneyFlowCategory: MoneyFlowCategory
    ): CheckStatusStr {

        if(name.isNotBlank()){
            val isDuplicate = name in categories.map{ it.name }
            if(!isDuplicate){
                try {
                    repository.addNewExpCat(
                        name = name,
                        amount = amount,
                        moneyFlowCategory = moneyFlowCategory)
                } catch (e: Exception){
                    return CheckStatusStr.UnKnownFailStatus
                }
                return CheckStatusStr.SuccessStatus
            } else {
                return CheckStatusStr.DuplicateFailStatus
            }
        } else {
            return CheckStatusStr.BlankFailStatus
        }
    }
}