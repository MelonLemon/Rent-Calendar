package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusNewFlat

class AddNewFlat(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(name: String, flatList: List<CategoryInfo>): CheckStatusNewFlat {

        if(name.isNotBlank()){
            val isDuplicate = name in flatList.map{ it.name }
            if(!isDuplicate){
                try {
                    repository.addNewFlat(name)
                } catch (e: Exception){
                    return CheckStatusNewFlat.UnKnownFailStatus
                }
                return CheckStatusNewFlat.SuccessStatus
            } else {
                return CheckStatusNewFlat.DuplicateFailStatus
            }
        } else {
            return CheckStatusNewFlat.BlankFailStatus
        }
    }
}