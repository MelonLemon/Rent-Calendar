package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import java.time.YearMonth

class AddExpenses (
    private val repository: HomeRepository
) {
    operator fun invoke(yearMonth: YearMonth, flatId: Int, catId: Int, amount: Int)  {


    }
}