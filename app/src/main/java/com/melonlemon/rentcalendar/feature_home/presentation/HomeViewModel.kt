package com.melonlemon.rentcalendar.feature_home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import com.melonlemon.rentcalendar.core.presentation.util.SendMessage
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.HomeUseCases
import com.melonlemon.rentcalendar.feature_home.presentation.util.*
import com.melonlemon.rentcalendar.feature_onboarding.presentation.OnBoardingUiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases,
    private val coreUseCases: CoreRentUseCases
): ViewModel() {


    private val _filterState = MutableStateFlow(FilterState(yearMonth = YearMonth.now()))
    val filterState  = _filterState.asStateFlow()

    private val _independentState = MutableStateFlow(IndependentState())
    val independentState  = _independentState.asStateFlow()

    private val _onHomeUiEvents = MutableSharedFlow<HomeUiEvents>()
    val onHomeUiEvents  = _onHomeUiEvents.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _monthlyCatExpenses = filterState.flatMapLatest{ filterState ->
        useCases.getExpensesByYM(
            yearMonth = filterState.yearMonth,
            flatId = filterState.selectedFlatId,
            moneyFlowCategory = MoneyFlowCategory.Regular
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _irregularCatExpenses = filterState.flatMapLatest{ filterState ->
        useCases.getExpensesByYM(
            yearMonth = filterState.yearMonth,
            flatId = filterState.selectedFlatId,
            moneyFlowCategory = MoneyFlowCategory.Irregular
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _rentInfo = filterState.flatMapLatest{ filterState ->
        useCases.getRentList(
            yearMonth = filterState.yearMonth,
            flatId = filterState.selectedFlatId)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val dependState = combine(_monthlyCatExpenses, _irregularCatExpenses, _rentInfo){ monthlyCatExpenses, irregularCatExpenses, rentInfo ->
        DependState(
            finResultsDisplay = useCases.getFinResults(
                flatId = filterState.value.selectedFlatId,
                year = filterState.value.yearMonth.year
            ),
            schedulePageState = SchedulePageDependState(
                rentInfo = rentInfo,
                schedulePageInfo = useCases.getSchedulePageInfo(rentInfo)
            ),
            expensesPageState = ExpensesPageDependState(
                monthlyExpenses = monthlyCatExpenses,
                irregularExpenses = irregularCatExpenses
            )
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DependState()
    )

    init {
        viewModelScope.launch {

            val allFlats = coreUseCases.getAllFlats()
            val categories = useCases.getExpCategories()
            _independentState.value = independentState.value.copy(
                flatState = independentState.value.flatState.copy(
                    listOfFlats = allFlats
                ),
                expCategoriesState = independentState.value.expCategoriesState.copy(
                    monthlyExpCategories = categories.monthlyExpCategories,
                    irregularExpCategories = categories.irregularExpCategories
                )
            )

            _onHomeUiEvents.emit(HomeUiEvents.ScrollFinancialResults)

        }
    }

    fun homeScreenEvents(event: HomeScreenEvents){
        when(event) {
            is HomeScreenEvents.SendMessage -> {
                viewModelScope.launch {
                    _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(event.message))
                }
            }

            is HomeScreenEvents.OnYearMonthChange -> {
                _filterState.value = filterState.value.copy(
                    yearMonth = event.yearMonth
                )
            }

            is HomeScreenEvents.OnNewFlatChanged -> {
                val newFlatState = independentState.value.flatState.copy(
                    newFlat = event.name
                )
                _independentState.value = independentState.value.copy(
                    flatState = newFlatState,
                    newBookedState = independentState.value.newBookedState.copy(
                        startDate = null,
                        endDate = null
                    )
                )

            }
            is HomeScreenEvents.OnAddNewFlatBtnClick -> {
                viewModelScope.launch {
                    val flatsState = independentState.value.flatState
                    val status = useCases.addNewFlat(
                        flatsState.newFlat,
                        flatsState.listOfFlats
                    )
                    if(status== CheckStatusStr.SuccessStatus){
                        val newFlatList = coreUseCases.getAllFlats()
                        val newFlatState = FlatState(
                            newFlat = "",
                            listOfFlats = newFlatList
                        )
                        _independentState.value = independentState.value.copy(
                            flatState = newFlatState
                        )
                    }
                    _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(status.message))

                }
            }

            is HomeScreenEvents.OnFlatClick -> {
                if(filterState.value.selectedFlatId!=event.id){
                    _filterState.value = filterState.value.copy(
                        selectedFlatId = event.id
                    )

                }
            }

            // Schedule Page
            is HomeScreenEvents.OnRentPaidChange -> {
                viewModelScope.launch {
                    val result = useCases.updatePaidStatus(id = event.id, isPaid = event.isPaid)
                    if(result != SimpleStatusOperation.OperationSuccess){
                        _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(R.string.err_msg_unknown_error))
                    }
                }
            }

            //Expenses Category

            is HomeScreenEvents.OnYearChanged -> {
                _filterState.value = filterState.value.copy(
                    yearMonth = YearMonth.of(event.year, filterState.value.yearMonth.monthValue)
                )

            }
            is HomeScreenEvents.OnMonthClick -> {
                _filterState.value = filterState.value.copy(
                    yearMonth = YearMonth.of(filterState.value.yearMonth.year, event.monthInt)
                )
                viewModelScope.launch {
                    _onHomeUiEvents.emit(HomeUiEvents.ScrollFinancialResults)
                }
            }

            is HomeScreenEvents.OnNewNameExpCatChanged -> {
                 val newState = independentState.value.expCategoriesState.copy(
                    newCategoryName = event.name
                )
                _independentState.value = independentState.value.copy(
                    expCategoriesState = newState
                )
            }
            is HomeScreenEvents.OnNewAmountExpCatChanged -> {

                val newState = independentState.value.expCategoriesState.copy(
                    newCategoryAmount = event.amount
                )
                _independentState.value = independentState.value.copy(
                    expCategoriesState = newState
                )
            }
            is HomeScreenEvents.OnAddNewExpCatBtnClick -> {

                viewModelScope.launch {
                    val status = useCases.addNewExpCat(
                        name = independentState.value.expCategoriesState.newCategoryName,
                        amount = independentState.value.expCategoriesState.newCategoryAmount,
                        categories = if(event.moneyFlowCategory==MoneyFlowCategory.Regular)
                            independentState.value.expCategoriesState.monthlyExpCategories else
                            independentState.value.expCategoriesState.irregularExpCategories,
                        moneyFlowCategory = event.moneyFlowCategory
                    )
                    if(status== CheckStatusStr.SuccessStatus){
                        val categories = useCases.getExpCategories()
                        val newState = ExpCategoriesState(
                            newCategoryName = "",
                            newCategoryAmount = 0,
                            monthlyExpCategories = categories.monthlyExpCategories,
                            irregularExpCategories = categories.irregularExpCategories
                        )
                        _independentState.value = independentState.value.copy(
                            expCategoriesState = newState
                        )
                    }
                    _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(status.message))

                }
            }

            is HomeScreenEvents.OnAmountExpChanged -> {
                if(event.monthlyIrregularToggle){

                    val newList = independentState.value.expCategoriesState.monthlyExpCategories.toMutableList()
                    newList[event.index] = newList[event.index].copy(
                        amount = event.amount
                    )
                    _independentState.value = independentState.value.copy(
                        expCategoriesState = independentState.value.expCategoriesState.copy(
                            monthlyExpCategories = newList
                        )
                    )

                } else {
                    val newList = independentState.value.expCategoriesState.irregularExpCategories.toMutableList()
                    newList[event.index] = newList[event.index].copy(
                        amount = event.amount
                    )
                    _independentState.value = independentState.value.copy(
                        expCategoriesState = independentState.value.expCategoriesState.copy(
                            irregularExpCategories = newList
                        )
                    )
                }

            }


            is HomeScreenEvents.OnExpensesAmountChange -> {
                viewModelScope.launch {
                    val result = useCases.updateExpenses(
                        id = event.expensesId,
                        amount = event.amount
                    )

                    if(result != SimpleStatusOperation.OperationSuccess) {
                        _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(R.string.err_msg_unknown_error))
                    }



                }

            }
            is HomeScreenEvents.OnCategoriesChanged -> {
                viewModelScope.launch {
                    val result = useCases.updateCategories(
                        categories = event.listChangedCategories
                    )
                    val categories = useCases.getExpCategories()
                    _independentState.value = independentState.value.copy(
                        expCategoriesState = independentState.value.expCategoriesState.copy(
                            monthlyExpCategories = categories.monthlyExpCategories,
                            irregularExpCategories = categories.irregularExpCategories
                        )
                    )
                    if(result != SimpleStatusOperation.OperationSuccess){
                        _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(R.string.err_msg_unknown_error))

                    }
                }

            }

            is HomeScreenEvents.OnExpensesAdd -> {

                viewModelScope.launch {
                    val result = useCases.addExpenses(
                        yearMonth = filterState.value.yearMonth,
                        flatId = filterState.value.selectedFlatId,
                        catId = event.catId,
                        amount = event.amount,
                        comment = event.categoryName
                    )

                    if(result != SimpleStatusOperation.OperationSuccess) {
                        _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(R.string.err_msg_unknown_error))
                    }
                }

            }
            // New Booked Events
            is HomeScreenEvents.OnNameBookedChanged -> {
                val newState = independentState.value.newBookedState.copy(
                    name = event.name
                )
                _independentState.value = independentState.value.copy(
                    newBookedState = newState
                )
            }
            is HomeScreenEvents.OnCommentBookedChanged -> {

                val newState = independentState.value.newBookedState.copy(
                    comment = event.comment
                )
                _independentState.value = independentState.value.copy(
                    newBookedState = newState
                )
            }
            is HomeScreenEvents.SetCalendarState -> {
                viewModelScope.launch {

                    val bookedDays = useCases.getBookedDays(year=event.year, flatId = filterState.value.selectedFlatId)
                    val newStartDate = if((independentState.value.newBookedState.startDate?.year ?: 0) == event.year)
                        independentState.value.newBookedState.startDate else null
                    val newEndDate = if((independentState.value.newBookedState.endDate?.year ?: 0) == event.year)
                        independentState.value.newBookedState.endDate else null
                    val newState = CalendarState(
                        year = event.year,
                        bookedDays = bookedDays,
                        startDate = newStartDate,
                        endDate = newEndDate
                    )
                    _independentState.value = independentState.value.copy(
                        calendarState = newState
                    )
                    _onHomeUiEvents.emit(HomeUiEvents.OpenCalendar)
                }

            }

            is HomeScreenEvents.OnDateRangeChanged -> {
                val nights = if(event.startDate!=null && event.endDate!=null)
                    ChronoUnit.DAYS.between(event.startDate, event.endDate).toInt() else 0

                val newState = independentState.value.newBookedState.copy(
                    startDate = event.startDate,
                    endDate = event.endDate,
                    nights = nights,
                    allMoney = nights*independentState.value.newBookedState.oneNightMoney
                )
                _independentState.value = independentState.value.copy(
                    newBookedState = newState
                )

            }
            is HomeScreenEvents.OnOneNightMoneyChange -> {
                val newState = independentState.value.newBookedState.copy(
                    oneNightMoney = event.money,
                    allMoney = if(independentState.value.newBookedState.nights!=0)
                        event.money*independentState.value.newBookedState.nights else event.money
                )
                _independentState.value = independentState.value.copy(
                    newBookedState = newState
                )
            }
            is HomeScreenEvents.OnAllMoneyChange -> {
                val newState = independentState.value.newBookedState.copy(
                    oneNightMoney =  if(independentState.value.newBookedState.nights!=0)
                        event.money/independentState.value.newBookedState.nights else event.money,
                    allMoney = event.money,
                )
                _independentState.value = independentState.value.copy(
                    newBookedState = newState
                )
            }
            is HomeScreenEvents.OnAddNewBookedBtnClick -> {
                viewModelScope.launch {
                    val result = useCases.addNewBooked(
                        newBookedState = independentState.value.newBookedState,
                        flatId = filterState.value.selectedFlatId
                    )
                    if (result==CheckStatusBooked.SuccessStatus){
                        val newState = NewBookedState(
                            startDate = null,
                            endDate = null,
                            name = "",
                            comment = "",
                            nights = 0,
                            oneNightMoney = 0,
                            allMoney = 0
                        )
                        _independentState.value = independentState.value.copy(
                            newBookedState = newState
                        )
                    }
                    _onHomeUiEvents.emit(HomeUiEvents.ShowMessage(result.message))

                }
            }

            //Currency dialog
            is HomeScreenEvents.OnCurrencySignChanged -> {
                _independentState.value = independentState.value.copy(
                    currencySign = event.sign
                )
            }
        }
    }
}