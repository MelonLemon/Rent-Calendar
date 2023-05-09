package com.melonlemon.rentcalendar.feature_onboarding.presentation.util


import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo
import java.util.Currency

data class OnBoardingState(
    val newFlat: String = "",
    val tempFlats: List<String> = emptyList(),
    val isMonthCat: Boolean = true,
    val newNameCat: String = "",
    val newAmountCat: Int = 0,
    val tempMonthlyExpCat: List<DisplayInfo> = emptyList(),
    val tempIrregularExpCat: List<DisplayInfo> = emptyList(),
    val listCurrency: List<Currency> = Currency.getAvailableCurrencies().toList(),
    val filteredListCurrency:  List<Currency> = listCurrency,
    val selectedCurrency: Currency = listCurrency[0],
    val textSearch: String = ""
)
