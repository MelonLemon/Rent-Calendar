package com.melonlemon.rentcalendar.feature_home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.HomeUseCases
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusNewFlat
import com.melonlemon.rentcalendar.feature_home.presentation.util.FlatsState
import com.melonlemon.rentcalendar.feature_home.presentation.util.HomeScreenEvents
import com.melonlemon.rentcalendar.feature_home.presentation.util.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val useCases: HomeUseCases
): ViewModel() {

    private val _finResults = MutableStateFlow<List<FinResultsDisplay>>(emptyList())
    val finResults  = _finResults.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState  = _homeScreenState.asStateFlow()

    private val _flatsState = MutableStateFlow(FlatsState())
    val flatsState  = _flatsState.asStateFlow()

    private val _selectedFlatId = MutableStateFlow(-1)
    val selectedFlatId  = _selectedFlatId.asStateFlow()


    init {
        viewModelScope.launch {
            _finResults.value = useCases.getFinResults(selectedFlatId.value)
        }
    }

    fun homeScreenEvents(event: HomeScreenEvents){
        when(event) {
            is HomeScreenEvents.OnNewFlatChanged -> {
                _flatsState.value = flatsState.value.copy(
                    newFlat = event.name
                )
            }
            is HomeScreenEvents.OnAddNewFlatBtnClick -> {
                viewModelScope.launch {
                    val status = useCases.addNewFlat(
                        flatsState.value.newFlat,
                        flatsState.value.listOfFlats
                    )
                    if(status== CheckStatusNewFlat.SuccessStatus){
                        val newFlatList = useCases.getAllFlats()
                        _flatsState.value = flatsState.value.copy(
                            newFlat = "",
                            listOfFlats = newFlatList,
                            checkStatusNewFlat = status
                        )
                    } else {
                        _flatsState.value = flatsState.value.copy(
                            checkStatusNewFlat = status
                        )
                    }
                }
            }
            is HomeScreenEvents.OnAddNewFlatComplete -> {
                _flatsState.value = flatsState.value.copy(
                   checkStatusNewFlat = CheckStatusNewFlat.UnCheckedStatus
                )
            }
            is HomeScreenEvents.OnFlatClick -> {
                _selectedFlatId.value = event.id
            }

            //Home Screen State
            is HomeScreenEvents.OnPageChange -> {
                _homeScreenState.value = homeScreenState.value.copy(
                    page = event.page
                )
            }


        }
    }
}