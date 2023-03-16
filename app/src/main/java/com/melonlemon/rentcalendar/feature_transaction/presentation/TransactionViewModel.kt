package com.melonlemon.rentcalendar.feature_transaction.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.TransactionsUseCases
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionScreenEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val useCases: TransactionsUseCases
): ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText  = _searchText.asStateFlow()

    private val _transFilterState = MutableStateFlow(TransFilterState())
    val transFilterState  = _transFilterState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactionsByMonth = transFilterState.flatMapLatest{ transFilterState ->
        useCases.getTransactions(transFilterState)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    @OptIn(FlowPreview::class)
    val transactionsByMonth  = searchText
        .debounce(500L)
        .combine(_transactionsByMonth)
        { searchText, transactionsByMonth  ->
            useCases.getFilteredTransactions(
                searchText = searchText,
                transactionsByMonth = transactionsByMonth)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _transactionsByMonth.value
        )

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
                val newFlatsId = transFilterState.value.selectedFlatsId
                if(event.id in newFlatsId) newFlatsId.toMutableList().remove(event.id)
                else newFlatsId.toMutableList().add(event.id)

                _transFilterState.value = transFilterState.value.copy(
                    selectedFlatsId = newFlatsId
                )
            }
            is TransactionScreenEvents.OnMonthClick -> {
                val newMonthsNum = transFilterState.value.chosenMonthsNum
                if(event.num in newMonthsNum) newMonthsNum.toMutableList().remove(event.num)
                else newMonthsNum.toMutableList().add(event.num)

                _transFilterState.value = transFilterState.value.copy(
                    chosenMonthsNum = newMonthsNum
                )


            }
            is TransactionScreenEvents.OnYearChange -> {
                _transFilterState.value = transFilterState.value.copy(
                    year = event.year
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