package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.model.Flats
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusStr

class AddNewFlat(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(name: String, flatList: List<CategoryInfo>): CheckStatusStr {

        if(name.isNotBlank()){
            val isDuplicate = name.lowercase().trimStart() in flatList.map{ it.name.lowercase() }
            if(!isDuplicate){
                try {
                    repository.addUpdateFlat(
                        Flats(
                            id = null,
                            name = name,
                            active = true
                        )
                    )
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