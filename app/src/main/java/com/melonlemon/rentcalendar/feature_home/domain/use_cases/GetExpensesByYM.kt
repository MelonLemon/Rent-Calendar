package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.presentation.util.MoneyFlowCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.time.YearMonth

class GetExpensesByYM(
    private val repository: HomeRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(yearMonth: YearMonth, flatId: Int, moneyFlowCategory: MoneyFlowCategory): Flow<List<ExpensesInfo>> {
        val expensesReg = repository.getExpensesByYM(
            moneyFlowCategory = moneyFlowCategory,
            yearMonth = yearMonth,
            flatId = flatId
        ).mapLatest { list ->
            list.map { expenses ->
                ExpensesInfo(
                    id = expenses.id!!,
                    categoryId = expenses.categoryId,
                    categoryName = expenses.comment,
                    paymentDate = expenses.paymentDate,
                    amount = expenses.amount
                )
            }

        }

        return expensesReg
    }
}