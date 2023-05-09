package com.melonlemon.rentcalendar.feature_onboarding.presentation.util

import android.content.Context
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.domain.model.DisplayInfo

class InitialSettings {
    private val listBaseFlat = listOf(R.string.main_flat)
    private val listMonthlyExpCat = listOf(R.string.monthly_cat_housing)
    private val listIrregularExpCat = listOf(R.string.cleaning, R.string.exp_cat_disposable, R.string.exp_cat_renovation)

    fun getInitialSettings(context: Context): SettingsInfo {
        val flats = listBaseFlat.map { flatId ->
            context.getString(flatId)
        }
        val monthlyExpCat = listMonthlyExpCat.map { categoryId ->
            DisplayInfo(
                name = context.getString(categoryId),
                amount = 0
            )
        }
        val irregularExpCat = listIrregularExpCat.map { categoryId ->
            DisplayInfo(
                name = context.getString(categoryId),
                amount = 0
            )
        }
        return SettingsInfo(
            flats=flats,
            monthlyExpCat=monthlyExpCat,
            irregularExpCat=irregularExpCat
        )
    }
}

data class SettingsInfo(
    val flats: List<String>,
    val monthlyExpCat: List<DisplayInfo>,
    val irregularExpCat: List<DisplayInfo>
)
