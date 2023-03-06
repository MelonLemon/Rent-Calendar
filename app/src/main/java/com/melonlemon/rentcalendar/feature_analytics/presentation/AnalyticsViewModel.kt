package com.melonlemon.rentcalendar.feature_analytics.presentation

import androidx.lifecycle.ViewModel
import com.melonlemon.rentcalendar.feature_analytics.domain.use_cases.AnalyticsUseCases
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.AnalyticsFilterState
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.AnalyticsScreenEvents
import com.melonlemon.rentcalendar.feature_analytics.presentation.util.FinSnapshotState
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnalyticsViewModel(
    private  val useCases: AnalyticsUseCases
): ViewModel() {

    private val _analyticsFilterState = MutableStateFlow(AnalyticsFilterState())
    val analyticsFilterState  = _analyticsFilterState.asStateFlow()

    private val _finSnapshotState = MutableStateFlow(FinSnapshotState())
    val finSnapshotState  = _finSnapshotState.asStateFlow()



    init{

    }

    fun analyticsScreenEvents(event: AnalyticsScreenEvents){
        when(event){
            is AnalyticsScreenEvents.OnReportChange -> {
                _analyticsFilterState.value = analyticsFilterState.value.copy(
                    chosenReport = event.report
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