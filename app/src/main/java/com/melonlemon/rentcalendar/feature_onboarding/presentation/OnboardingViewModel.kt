package com.melonlemon.rentcalendar.feature_onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.core.data.repository.DataStoreRepository
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransFilterState
import com.melonlemon.rentcalendar.feature_transaction.presentation.util.TransactionScreenEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val coreUseCases: CoreRentUseCases,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {


    private val _showErrorMessage = MutableStateFlow(false)
    val showErrorMessage  = _showErrorMessage.asStateFlow()

    private val _finishOnboarding = MutableStateFlow(false)
    val finishOnboarding  = _finishOnboarding.asStateFlow()

    fun onboardingScreenEvents(event: OnboardingEvents){
        when(event){
            is OnboardingEvents.OnSaveBaseOptionClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = coreUseCases.saveBaseOption(
                        flats = event.flats,
                        monthlyExpCat = event.monthlyExpCategories,
                        irregExpCat = event.irregExpCategories
                    )
                    if(result== SimpleStatusOperation.OperationSuccess){
                        dataStoreRepository.saveOnBoardingState(true)
                        _finishOnboarding.value = true
                    } else {
                        _showErrorMessage.value = true
                    }

                }

            }
            is OnboardingEvents.RefreshErrorMessageState -> {
                _showErrorMessage.value = false
            }

        }
    }

}

