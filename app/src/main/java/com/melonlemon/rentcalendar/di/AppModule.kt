package com.melonlemon.rentcalendar.di

import android.app.Application
import androidx.room.Room
import com.melonlemon.rentcalendar.core.data.data_source.DatabaseInitializer
import com.melonlemon.rentcalendar.core.data.data_source.RentDao
import com.melonlemon.rentcalendar.core.data.data_source.RentDatabase
import com.melonlemon.rentcalendar.core.data.repository.AnalyticsRepositoryImpl
import com.melonlemon.rentcalendar.core.data.repository.CoreRentRepositoryImpl
import com.melonlemon.rentcalendar.core.data.repository.HomeRepositoryImpl
import com.melonlemon.rentcalendar.core.data.repository.TransactionRepositoryImpl
import com.melonlemon.rentcalendar.core.domain.repository.CoreRentRepository
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import com.melonlemon.rentcalendar.core.domain.use_cases.GetAllFlats
import com.melonlemon.rentcalendar.feature_analytics.domain.repository.AnalyticsRepository
import com.melonlemon.rentcalendar.feature_analytics.domain.use_cases.*
import com.melonlemon.rentcalendar.feature_home.domain.repository.HomeRepository
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.*
import com.melonlemon.rentcalendar.feature_transaction.domain.repository.TransactionsRepository
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.GetFilteredTransactions
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.GetTransactions
import com.melonlemon.rentcalendar.feature_transaction.domain.use_cases.TransactionsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRentDatabase(
        app: Application,
        catalogueProvider: Provider<RentDao>,
    ): RentDatabase {
        return Room.databaseBuilder(
            app,
            RentDatabase::class.java,
            RentDatabase.DATABASE_NAME
        ).addCallback(
            DatabaseInitializer(catalogueProvider)
        ).build()
    }

    @Provides
    @Singleton
    fun provideRentDao(db: RentDatabase): RentDao = db.rentDao

    @Provides
    @Singleton
    fun provideCoreRentRepository(db: RentDatabase): CoreRentRepository {
        return CoreRentRepositoryImpl(db.rentDao)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(db: RentDatabase): HomeRepository {
        return HomeRepositoryImpl(db.rentDao)
    }
    @Provides
    @Singleton
    fun provideAnalyticsRepository(db: RentDatabase): AnalyticsRepository {
        return AnalyticsRepositoryImpl(db.rentDao)
    }
    @Provides
    @Singleton
    fun provideTransactionRepository(db: RentDatabase): TransactionsRepository {
        return TransactionRepositoryImpl(db.rentDao)
    }

    @Provides
    @Singleton
    fun provideHomeUseCases(repository: HomeRepository): HomeUseCases {
        return HomeUseCases(
            getFinResults = GetFinResults(repository),
            addNewFlat = AddNewFlat(repository),
            updatePaidStatus = UpdatePaidStatus(repository),
            getRentList = GetRentList(repository),
            getSchedulePageState = GetSchedulePageState(),
            addNewExpCat = AddNewExpCat(repository),
            getExpCategories = GetExpCategories(repository),
            addNewBooked = AddNewBooked(repository),
            updateCategories = UpdateCategories(repository),
            addExpenses = AddExpenses(repository),
            getExpensesByYM = GetExpensesByYM(repository),
            updateExpenses = UpdateExpenses(repository),
            saveBaseOption = SaveBaseOption(repository),
            getBookedDays = GetBookedDays(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCoreRentCases(repository: CoreRentRepository): CoreRentUseCases {
        return CoreRentUseCases(
            getAllFlats = GetAllFlats(repository)
        )
    }



    @Provides
    @Singleton
    fun provideAnalyticsUseCases(repository: AnalyticsRepository): AnalyticsUseCases {
        return AnalyticsUseCases(
            getCashFlowInfo = GetCashFlowInfo(repository),
            getInvestmentReturn = GetInvestmentReturn(repository),
            getIncomeStatement = GetIncomeStatement(repository),
            getBookedReport = GetBookedReport(repository)
        )
    }

    @Provides
    @Singleton
    fun provideTransactionsUseCases(repository: TransactionsRepository): TransactionsUseCases {
        return TransactionsUseCases(
            getTransactions = GetTransactions(repository),
            getFilteredTransactions = GetFilteredTransactions()
        )
    }

}