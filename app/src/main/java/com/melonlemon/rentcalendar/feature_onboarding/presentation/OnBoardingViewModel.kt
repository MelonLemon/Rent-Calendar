package com.melonlemon.rentcalendar.feature_onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.data.repository.StoreCurrencyRepository
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_onboarding.presentation.util.OnBoardingEvents
import com.melonlemon.rentcalendar.feature_onboarding.presentation.util.OnBoardingState
import com.melonlemon.rentcalendar.feature_onboarding.presentation.util.OnBoardingUiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val coreUseCases: CoreRentUseCases,
    private val storeCurrencyRepository: StoreCurrencyRepository
): ViewModel() {

    private val _onBoardingState = MutableStateFlow(OnBoardingState())
    val onBoardingState  = _onBoardingState.asStateFlow()

    private val _onBoardingUiEvents = MutableSharedFlow<OnBoardingUiEvents>()
    val onBoardingUiEvents  = _onBoardingUiEvents.asSharedFlow()

    fun onBoardingScreenEvents(event: OnBoardingEvents){
        when(event){

            is OnBoardingEvents.InitSettings -> {
                val newOnBoardingState = OnBoardingState(
                    tempFlats = event.settingsInfo.flats,
                    tempMonthlyExpCat = event.settingsInfo.monthlyExpCat,
                    tempIrregularExpCat = event.settingsInfo.irregularExpCat
                )
                _onBoardingState.value = newOnBoardingState
                viewModelScope.launch {
                    _onBoardingUiEvents.emit(OnBoardingUiEvents.FinishInitSettings)
                }

            }
            is OnBoardingEvents.OnSaveBaseOptionClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = coreUseCases.saveBaseOption(
                        flats = onBoardingState.value.tempFlats,
                        monthlyExpCat = onBoardingState.value.tempMonthlyExpCat,
                        irregExpCat = onBoardingState.value.tempIrregularExpCat
                    )
                    if(result == SimpleStatusOperation.OperationSuccess){
                        storeCurrencyRepository.updateCurrency(onBoardingState.value.selectedCurrency)
                        _onBoardingUiEvents.emit(OnBoardingUiEvents.FinishOnBoarding)
                    } else {
                        _onBoardingUiEvents.emit(OnBoardingUiEvents.ShowErrorMessage(R.string.err_msg_onboarding))
                    }

                }

            }
            is OnBoardingEvents.SendMessage -> {
                viewModelScope.launch {
                    _onBoardingUiEvents.emit(OnBoardingUiEvents.ShowErrorMessage(event.message))
                }
            }
            //FLAT PAGE

            is OnBoardingEvents.OnNewNameChanged -> {
                _onBoardingState.value = onBoardingState.value.copy(
                    newFlat=event.name
                )
            }
            is OnBoardingEvents.OnNewFlatAdd -> {
                if(onBoardingState.value.newFlat.trim().lowercase() !in onBoardingState.value.tempFlats.map{it.lowercase()}){
                    val newList = onBoardingState.value.tempFlats.toMutableList()
                    newList.add(onBoardingState.value.newFlat.trim())
                    _onBoardingState.value = onBoardingState.value.copy(
                        tempFlats = newList,
                        newFlat = ""
                    )}
            }
            is OnBoardingEvents.OnNameFlatChanged -> {
                val newList = onBoardingState.value.tempFlats.toMutableList()
                newList[event.index] = event.name
                _onBoardingState.value = onBoardingState.value.copy(
                    tempFlats = newList
                )
            }

            //EXPENSES CATEGORIES PAGE
            is OnBoardingEvents.OnSegmentBtnClick -> {
                _onBoardingState.value = onBoardingState.value.copy(
                    isMonthCat = event.isMonthCatChosen
                )
            }
            is OnBoardingEvents.OnNewAmountChange -> {
                _onBoardingState.value = onBoardingState.value.copy(
                    newAmountCat = event.amount
                )
            }
            is OnBoardingEvents.OnNewNameChange -> {
                _onBoardingState.value = onBoardingState.value.copy(
                    newNameCat = event.name
                )
            }
            is OnBoardingEvents.OnNewCatAdd -> {
                val newList = if(onBoardingState.value.isMonthCat) onBoardingState.value.tempMonthlyExpCat.toMutableList()
                else onBoardingState.value.tempIrregularExpCat.toMutableList()
                if(onBoardingState.value.newNameCat.trim().lowercase() !in newList.map{ it.name.lowercase()}) {
                    newList.add(
                        DisplayInfo(
                            name = onBoardingState.value.newNameCat.trim(),
                            amount = onBoardingState.value.newAmountCat)
                    )
                    if(onBoardingState.value.isMonthCat){
                        _onBoardingState.value = onBoardingState.value.copy(
                            tempMonthlyExpCat = newList,
                            newNameCat = "",
                            newAmountCat = 0,
                        )
                    } else {
                        _onBoardingState.value = onBoardingState.value.copy(
                            tempIrregularExpCat = newList,
                            newNameCat = "",
                            newAmountCat = 0,
                        )
                    }

                }

            }
            is OnBoardingEvents.OnNameCatChanged -> {

                val newList = if(onBoardingState.value.isMonthCat)  onBoardingState.value.tempMonthlyExpCat.toMutableList() else
                    onBoardingState.value.tempIrregularExpCat.toMutableList()
                newList[event.index] = newList[event.index].copy(
                    name = event.name
                )
                if(onBoardingState.value.isMonthCat){
                    _onBoardingState.value = onBoardingState.value.copy(
                        tempMonthlyExpCat = newList
                    )
                } else {
                    _onBoardingState.value = onBoardingState.value.copy(
                        tempIrregularExpCat = newList
                    )
                }

            }
            is OnBoardingEvents.OnAmountCatChanged -> {

                val newList = if(onBoardingState.value.isMonthCat)  onBoardingState.value.tempMonthlyExpCat.toMutableList() else
                    onBoardingState.value.tempIrregularExpCat.toMutableList()
                newList[event.index] = newList[event.index].copy(
                    amount = event.amount
                )
                if(onBoardingState.value.isMonthCat){
                    _onBoardingState.value = onBoardingState.value.copy(
                        tempMonthlyExpCat = newList
                    )
                } else {
                    _onBoardingState.value = onBoardingState.value.copy(
                        tempIrregularExpCat = newList
                    )
                }

            }

            //CURRENCY PAGE
            is OnBoardingEvents.OnTextSearchChanged -> {
                val newFilteredList = if(event.text.isNotBlank())
                    onBoardingState.value.listCurrency.filter { it.getDisplayName(Locale.ENGLISH).contains(event.text, ignoreCase = true) }
                else onBoardingState.value.listCurrency
                _onBoardingState.value = onBoardingState.value.copy(
                    textSearch = event.text,
                    filteredListCurrency = newFilteredList
                )
            }
            is OnBoardingEvents.OnTextSearchCancel -> {
                _onBoardingState.value = onBoardingState.value.copy(
                    textSearch = "",
                    filteredListCurrency = onBoardingState.value.listCurrency
                )
            }
            is OnBoardingEvents.OnCurrencyClick -> {
                _onBoardingState.value = onBoardingState.value.copy(
                    selectedCurrency = event.currency
                )
            }





        }
    }

}

