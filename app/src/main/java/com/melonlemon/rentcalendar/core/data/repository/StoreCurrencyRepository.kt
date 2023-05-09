package com.melonlemon.rentcalendar.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.melonlemon.rentcalendar.core.data.repository.StoreCurrencyRepository.PreferencesKey.onCurrencyKey
import com.melonlemon.rentcalendar.core.data.util.CURRENCY_PREFERENCE_NAME
import com.melonlemon.rentcalendar.core.data.util.CURRENCY_SYMBOL
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.util.*


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = CURRENCY_PREFERENCE_NAME)

class StoreCurrencyRepository(context: Context) {

    private object PreferencesKey {
        val onCurrencyKey = stringPreferencesKey(name = CURRENCY_SYMBOL)
    }

    private val dataStore = context.dataStore

    suspend fun updateCurrency(currency: Currency) {
        dataStore.edit { preferences ->
            preferences[onCurrencyKey] = currency.symbol
        }
    }

    fun getCurrencySymbol(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[onCurrencyKey] ?: ""
            }
    }

}