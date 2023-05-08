package com.melonlemon.rentcalendar.feature_transaction.presentation.util

import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.model.Currency
import java.time.YearMonth

data class TransFilterState(
    val transactionType: TransactionType = TransactionType.AllTransaction,
    val flats: List<CategoryInfo> = emptyList(),
    val selectedFlatsId: List<Int> = listOf(-1),
    val chosenPeriod: TransactionPeriod = TransactionPeriod.YearPeriod,
    val years: List<CategoryInfo> = listOf(CategoryInfo(id=0, name=YearMonth.now().year.toString())),
    val selectedYearId: Int = 0,
    val chosenMonthsNum: List<Int> = listOf(1),
    val transFilterInit: Boolean = false,
    val currency: String = ""
){
    fun getSelectedFlatsName(): List<String> {
        val  flatsName = mutableListOf<String>()
        val idList = flats.map{ it.id}
        selectedFlatsId.forEach { id->
            if(idList.contains(id)){
                val name =  flats.find { it.id==id }
                if (name != null) {
                    flatsName.add(name.name)
                }
            }
        }
        return flatsName
    }
}
