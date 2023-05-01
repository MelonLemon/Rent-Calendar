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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _finSnapshotState = MutableStateFlow(InvestmentReturnState())
    val finSnapshotState  = _finSnapshotState.asStateFlow()

    private val _chosenReport = MutableStateFlow<Reports>(Reports.IncomeStatement)
    val chosenReport  = _chosenReport.asStateFlow()

    private val _listOfFlats = MutableStateFlow<List<CategoryInfo>>(emptyList())
    val listOfFlats  = _listOfFlats.asStateFlow()

    private val _listOfYears = MutableStateFlow(listOf(
        CategoryInfo(id=0, name = YearMonth.now().year.toString())
    ))
    val listOfYears  = _listOfYears.asStateFlow()

    private val _incomeStatementState = MutableStateFlow<List<IncomeStatementInfo>>(emptyList())
    val incomeStatementState  = _incomeStatementState.asStateFlow()

    private val _cashFlowState = MutableStateFlow<List<CashFlowInfo>>(emptyList())
    val cashFlowState  = _cashFlowState.asStateFlow()

    private val _bookedReportState = MutableStateFlow(BookedReportState())
    val bookedReportState  = _bookedReportState.asStateFlow()



    init{
        viewModelScope.launch {
            _listOfFlats.value = coreUseCases.getAllFlats()
            _listOfYears.value = coreUseCases.getActiveYears()
            _analyticsFilterState.value = analyticsFilterState.value.copy(
                selectedYearId = listOfYears.value[0].id
            )
            val selectedYear = listOfYears.value[0].name.toIntOrNull() ?: 0
            _finSnapshotState.value = useCases.getInvestmentReturn(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
            _incomeStatementState.value = useCases.getIncomeStatement(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
            _cashFlowState.value = useCases.getCashFlowInfo(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
            _bookedReportState.value = useCases.getBookedReport(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
        }


    }

    fun analyticsScreenEvents(event: AnalyticsScreenEvents){
        when(event){
            is AnalyticsScreenEvents.OnReportChange -> {
                _chosenReport.value = event.report
            }
            is AnalyticsScreenEvents.OnFlatClick -> {
                viewModelScope.launch {
                    _analyticsFilterState.value =  analyticsFilterState.value.copy(
                        selectedFlatId = event.id
                    )
                    val selectedYear = listOfYears.value.find{ it.id == analyticsFilterState.value.selectedYearId}?.name?.toInt() ?: YearMonth.now().year
                    _finSnapshotState.value = useCases.getInvestmentReturn(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
                    _incomeStatementState.value = useCases.getIncomeStatement(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
                    _cashFlowState.value = useCases.getCashFlowInfo(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
                    _bookedReportState.value = useCases.getBookedReport(year=selectedYear, flatId=analyticsFilterState.value.selectedFlatId)
                }
            }

            is AnalyticsScreenEvents.OnTotalPurchaseChange -> {
                _finSnapshotState.value = finSnapshotState.value.copy(
                    totalPurchasePrice = event.price
                )
            }


        }
    }
}