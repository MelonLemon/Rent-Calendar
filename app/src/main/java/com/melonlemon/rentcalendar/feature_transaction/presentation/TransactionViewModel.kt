package com.melonlemon.rentcalendar.feature_transaction.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.core.domain.model.CategoryInfo
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.TransactionsUseCases
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionScreenEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val coreUseCases: CoreRentUseCases,
    private val useCases: TransactionsUseCases
): ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText  = _searchText.asStateFlow()

    private val _transFilterState = MutableStateFlow(TransFilterState())
    val transFilterState  = _transFilterState.asStateFlow()

    private val _isDownloading = MutableStateFlow(true)
    val isDownloading = _isDownloading.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactionsByMonth = transFilterState.flatMapLatest{ transFilterState ->
        useCases.getTransactions(
            flatIds = transFilterState.selectedFlatsId,
            year = transFilterState.years.find{it.id==transFilterState.selectedYearId}!!.name.toIntOrNull() ?: 0,
            transactionType = transFilterState.transactionType,
            currencySign = "$" //change
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    @OptIn(FlowPreview::class)
    val transactionsByMonth  = searchText
        .debounce(500L)
        .onEach { _isDownloading.update { true } }
        .combine(_transactionsByMonth)
        { searchText, transactionsByMonth  ->
            useCases.getTransactions(
                flatIds = transFilterState.value.selectedFlatsId,
                year = transFilterState.value.years.find{it.id==transFilterState.value.selectedYearId}!!.name.toIntOrNull() ?: 0,
                transactionType = transFilterState.value.transactionType,
                currencySign = "$", //change
                searchText = searchText
            ).first()
        }.onEach { _isDownloading.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _transactionsByMonth.value
        )

    init {
        viewModelScope.launch {
            val flats = coreUseCases.getAllFlats()
            val years = coreUseCases.getActiveYears()
            _transFilterState.value = transFilterState.value.copy(
                flats = flats,
                years = years,
                selectedYearId = years[0].id,
                selectedFlatsId = listOf(flats[0].id)
            )
        }

    }

    fun transactionScreenEvents(event: TransactionScreenEvents){
        when(event){
            is TransactionScreenEvents.OnSearchTextChanged -> {
                _searchText.value = event.text
            }
            is TransactionScreenEvents.OnCancelClicked -> {
                _searchText.value = ""
            }
            is TransactionScreenEvents.OnTransactionTypeClick -> {
                _transFilterState.value = transFilterState.value.copy(
                    transactionType = event.transactionType
                )
            }
            is TransactionScreenEvents.OnFlatsClick -> {
                if(event.id==-1){
                    val newFlatsId = listOf(-1)
                    _transFilterState.value = transFilterState.value.copy(
                        selectedFlatsId = newFlatsId
                    )
                } else {
                    val newFlatsId = transFilterState.value.selectedFlatsId
                    if(event.id in newFlatsId) newFlatsId.toMutableList().remove(event.id)
                    else newFlatsId.toMutableList().add(event.id)

                    _transFilterState.value = transFilterState.value.copy(
                        selectedFlatsId = newFlatsId
                    )
                }

            }
            is TransactionScreenEvents.OnMonthClick -> {
                val newMonthsNum = transFilterState.value.chosenMonthsNum
                if(event.num in newMonthsNum) newMonthsNum.toMutableList().remove(event.num)
                else newMonthsNum.toMutableList().add(event.num)

                _transFilterState.value = transFilterState.value.copy(
                    chosenMonthsNum = newMonthsNum
                )


            }
            is TransactionScreenEvents.OnYearClick -> {
                _transFilterState.value = transFilterState.value.copy(
                    selectedYearId = event.yearId
                )
            }
            is TransactionScreenEvents.OnYearMonthClick -> {
                _transFilterState.value = transFilterState.value.copy(
                    chosenPeriod = event.transactionPeriod
                )
            }

        }
    }
}