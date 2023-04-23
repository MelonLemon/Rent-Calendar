package com.melonlemon.rentcalendar.feature_onboarding.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.Screens
import com.melonlemon.rentcalendar.core.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



class SplashViewModel@Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading  = _isLoading.asStateFlow()


    private val _startDestination = MutableStateFlow(Screens.OnBoardingScreen.route)
    val startDestination  = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreRepository.readOnBoardingState().collect { completed ->
                if (completed) {
                    _startDestination.value = Screens.HomeScreen.route
                } else {
                    _startDestination.value = Screens.OnBoardingScreen.route
                }
            }
            _isLoading.value = false
        }
    }
}