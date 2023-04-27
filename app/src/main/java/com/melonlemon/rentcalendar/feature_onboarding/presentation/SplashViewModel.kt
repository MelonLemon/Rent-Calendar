package com.melonlemon.rentcalendar.feature_onboarding.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.Screens
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val coreUseCases: CoreRentUseCases,
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading  = _isLoading.asStateFlow()


    private val _startDestination = MutableStateFlow(Screens.OnBoardingScreen.route)
    val startDestination  = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            println("start of Splash Screen: loading: ${isLoading.value}, startDestination: ${startDestination.value}")
            val flats = coreUseCases.getAllFlats()
            if (flats.isNotEmpty()) {
                _startDestination.value = Screens.HomeScreen.route
            } else {
                _startDestination.value = Screens.OnBoardingScreen.route
            }
            _isLoading.value = false
            println("End of Splash Screen: loading: ${isLoading.value}, startDestination: ${startDestination.value}")
        }
    }
}