package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository

class UpdateExpenses(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: Int, amount: Int): SimpleStatusOperation {
        try {
            repository.updateExpenses(
                id = id,
                amount = amount
            )
        } catch (e: Exception) {
            return SimpleStatusOperation.OperationFail
        }
        return SimpleStatusOperation.OperationSuccess

    }
}