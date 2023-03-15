package com.melonlemon.rentcalendar.feature_home.domain.use_cases

import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

class UpdateFixAmountCat(
    private val repository: HomeRepository
) {
    operator fun invoke(yearMonth: YearMonth, flatId: Int, catId: Int, amount: Int)  {


    }
}