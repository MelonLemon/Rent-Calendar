package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository

class UpdatePaidStatus(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: Int, isPaid: Boolean): SimpleStatusOperation {
        try {
            repository.updatePaidStatus(id, isPaid)
        } catch (e: Exception) {
            return SimpleStatusOperation.OperationFail
        }
        return SimpleStatusOperation.OperationSuccess

    }
}