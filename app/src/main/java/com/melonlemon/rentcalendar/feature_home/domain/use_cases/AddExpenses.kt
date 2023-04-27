package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import java.time.YearMonth

class AddExpenses (
    private val repository: HomeRepository
) {
    suspend operator fun invoke(
        yearMonth: YearMonth,
        flatId: Int,
        catId: Int,
        amount: Int,
        comment: String): SimpleStatusOperation {
        try {
            repository.addExpenses(
                yearMonth = yearMonth,
                flatId = flatId,
                catId = catId,
                amount = amount,
                comment = comment
            )
        } catch (e: Exception) {
            return SimpleStatusOperation.OperationFail
        }
        return SimpleStatusOperation.OperationSuccess

    }
}