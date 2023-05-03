package com.melonlemon.rentcalendar.feature_analytics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import com.melonlemon.rentcalendar.feature_analytics.domain.model.CashFlowInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.model.IncomeStatementInfo
import com.melonlemon.rentcalendar.feature_analytics.domain.use_cases.AnalyticsUseCases
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val coreUseCases: CoreRentUseCases,
    private  val useCases: AnalyticsUseCases
): ViewModel() {

    private val _analyticsFilterState = MutableStateFlow(AnalyticsFilterState())
    val analyticsFilterState  = _analyticsFilterState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _analyticsDependState = _analyticsFilterState.flatMapLatest { filterState ->
        val selectedYear = analyticsIndependentState.value.listOfYears[filterState.selectedYearId].name.toIntOrNull() ?: 0
        val finSnapshotState  = useCases.getInvestmentReturn(year=selectedYear, flatId=filterState.selectedFlatId)
        val incomeStatementState  = useCases.getIncomeStatement(year=selectedYear, flatId=filterState.selectedFlatId)
        val cashFlowState  = useCases.getCashFlowInfo(year=selectedYear, flatId=filterState.selectedFlatId)
        val bookedReportState  = useCases.getBookedReport(year=selectedYear, flatId=filterState.selectedFlatId)
        flow{
            emit(AnalyticsDependState(
                finSnapshotState=finSnapshotState,
                incomeStatementState=incomeStatementState,
                cashFlowState=cashFlowState,
                bookedReportState=bookedReportState
            ))
        }

    }
    val analyticsDependState  = _analyticsDependState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AnalyticsDependState()
    )

    private val _analyticsIndependentState = MutableStateFlow(AnalyticsIndependentState())
    val analyticsIndependentState  = _analyticsIndependentState.asStateFlow()


    init{
        viewModelScope.launch {
            val listOfFlats  = coreUseCases.getAllFlats()
            val listOfYears  = coreUseCases.getActiveYears()
            _analyticsIndependentState.value = analyticsIndependentState.value.copy(
                listOfFlats=listOfFlats,
                listOfYears=listOfYears
            )
            _analyticsFilterState.value = analyticsFilterState.value.copy(
                selectedYearId = analyticsIndependentState.value.listOfYears[0].id
            )

        }


    }

    fun analyticsScreenEvents(event: AnalyticsScreenEvents){
        when(event){
            is AnalyticsScreenEvents.OnReportChange -> {
                _analyticsIndependentState.value = analyticsIndependentState.value.copy(
                    chosenReport = event.report
                )
            }
            is AnalyticsScreenEvents.OnFlatClick -> {
                _analyticsFilterState.value =  analyticsFilterState.value.copy(
                    selectedFlatId = event.id
                )
            }
            is AnalyticsScreenEvents.OnYearClick -> {
                _analyticsFilterState.value =  analyticsFilterState.value.copy(
                    selectedYearId = event.id
                )
            }

            is AnalyticsScreenEvents.OnTotalPurchaseChange -> {
                _analyticsIndependentState.value = analyticsIndependentState.value.copy(
                    totalPurchasePrice = event.price
                )
            }


        }
    }


}