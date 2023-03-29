package com.melonlemon.rentcalendar.core.data.data_source

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.melonlemon.rentcalendar.core.data.util.*
import com.melonlemon.rentcalendar.core.domain.model.CategoryType
import com.melonlemon.rentcalendar.core.domain.model.Flats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class DatabaseInitializer(
    private val rentProvider: Provider<RentDao>
): RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        populateCategoryType()
    }

    private suspend fun populateCategoryType() {
        rentProvider.get().addCategoryType(
            CategoryType(
                id = REGULAR_EXP,
                isRegular = true,
            ))

        rentProvider.get().addCategoryType(
            CategoryType(
                id = IRREGULAR_EXP,
                isRegular = false,
            ))
    }



}