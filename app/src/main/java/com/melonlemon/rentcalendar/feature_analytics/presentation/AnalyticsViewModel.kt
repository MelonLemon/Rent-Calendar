package com.melonlemon.rentcalendar.feature_analytics.presentation

import androidx.lifecycle.ViewModel
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.model.IncomeStatementInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.use_cases.AnalyticsUseCases
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnalyticsViewModel(
    private  val useCases: AnalyticsUseCases
): ViewModel() {

    private val _analyticsFilterState = MutableStateFlow(AnalyticsFilterState())
    val analyticsFilterState  = _analyticsFilterState.asStateFlow()

    private val _finSnapshotState = MutableStateFlow(InvestmentReturnState())
    val finSnapshotState  = _finSnapshotState.asStateFlow()

    private val _chosenReport = MutableStateFlow<Reports>(Reports.IncomeStatement)
    val chosenReport  = _chosenReport.asStateFlow()

    private val _listOfFlats = MutableStateFlow<List<CategoryInfo>>(emptyList())
    val listOfFlats  = _listOfFlats.asStateFlow()

    private val _incomeStatementState = MutableStateFlow(IncomeStatementState())
    val incomeStatementState  = _incomeStatementState.asStateFlow()

    private val _listOfIncomeStInfo = MutableStateFlow<List<IncomeStatementInfo>>(emptyList())
    val listOfIncomeStInfo  = _listOfIncomeStInfo.asStateFlow()

    private val _cashFlowState = MutableStateFlow(CashFlowState())
    val cashFlowState  = _cashFlowState.asStateFlow()

    private val _listOfCashFlowInfo = MutableStateFlow<List<CashFlowState>>(emptyList())
    val listOfCashFlowInfo  = _listOfCashFlowInfo.asStateFlow()

    private val _bookedReportState = MutableStateFlow(BookedReportState())
    val bookedReportState  = _bookedReportState.asStateFlow()



    init{

    }

    fun analyticsScreenEvents(event: AnalyticsScreenEvents){
        when(event){
            is AnalyticsScreenEvents.OnReportChange -> {
                _chosenReport.value = event.report
            }
            is AnalyticsScreenEvents.OnFlatClick -> {
                _analyticsFilterState.value =  analyticsFilterState.value.copy(
                    selectedFlatId = event.id
                )
            }

            is AnalyticsScreenEvents.OnTotalPurchaseChange -> {
                _finSnapshotState.value = finSnapshotState.value.copy(
                    totalPurchasePrice = event.price
                )
            }


        }
    }
}