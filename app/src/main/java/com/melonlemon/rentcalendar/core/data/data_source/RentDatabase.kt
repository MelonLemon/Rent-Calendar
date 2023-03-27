package com.melonlemon.rentcalendar.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.melonlemon.rentcalendar.core.domain.model.*
import com.melonlemon.rentcalendar.core.domain.util.Converters


@Database(
    entities = [Flats::class, Schedule::class, Person::class, Payment::class,
        Expenses::class, Category::class, CategoryType::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RentDatabase: RoomDatabase() {

    abstract val rentDao: RentDao

    companion object {
        const val DATABASE_NAME = "rent_db"
    }

}