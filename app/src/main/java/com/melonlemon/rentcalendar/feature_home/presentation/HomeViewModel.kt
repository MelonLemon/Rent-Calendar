package com.melonlemon.rentcalendar.feature_home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.domain.use_cases.CoreRentUseCases
import com.melonlemon.rentcalendar.core.presentation.util.SendMessage
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.HomeUseCases
import com.melonlemon.rentcalendar.feature_home.presentation.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases,
    private val coreUseCases: CoreRentUseCases
): ViewModel() {

    private val _finResults = MutableStateFlow<List<FinResultsDisplay>>(emptyList())
    val finResults  = _finResults.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState  = _homeScreenState.asStateFlow()

    private val _flatsState = MutableStateFlow(FlatsState())
    val flatsState  = _flatsState.asStateFlow()

    private val _filterState = MutableStateFlow(ScheduleFilterState(yearMonth = YearMonth.now()))
    val filterState  = _filterState.asStateFlow()

    private val _expensesCategoriesState = MutableStateFlow(ExpensesCategoriesState())
    val expensesCategoriesState  = _expensesCategoriesState.asStateFlow()

    private val _newBookedState = MutableStateFlow(NewBookedState())
    val newBookedState  = _newBookedState.asStateFlow()

    private val _calendarState = MutableStateFlow(CalendarState())
    val calendarState  = _calendarState.asStateFlow()

    private val _messageState = MutableStateFlow(SendMessage())
    val messageState  = _messageState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _rentList = filterState.flatMapLatest{ filterState ->
        useCases.getRentList(yearMonth = filterState.yearMonth, flatId = filterState.selectedFlatId)
    }

    val rentList  = _rentList.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _selectedExpenses = MutableStateFlow(
        ExpensesInfo(
            id = -1,
            categoryId = -1,
            categoryName = "",
            paymentDate = LocalDate.now(),
            amount = 0)
    )
    val selectedExpenses  = _selectedExpenses.asStateFlow()

    private val _moneyFlowCategory = MutableStateFlow<MoneyFlowCategory>(MoneyFlowCategory.Regular)
    private val moneyFlowCategory  = _moneyFlowCategory.asStateFlow()

    private val _displayExpCategories = MutableStateFlow(DisplayExpenses())
    val displayExpCategories  = _displayExpCategories.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _displayExpensesReg = filterState.flatMapLatest{ filterState ->
        useCases.getExpensesByYM(
            yearMonth = filterState.yearMonth,
            flatId = filterState.selectedFlatId,
            moneyFlowCategory = MoneyFlowCategory.Regular
        )
    }
    val displayExpensesReg = _displayExpensesReg.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _displayExpensesIr = filterState.flatMapLatest{ filterState ->
        useCases.getExpensesByYM(
            yearMonth = filterState.yearMonth,
            flatId = filterState.selectedFlatId,
            moneyFlowCategory = MoneyFlowCategory.Irregular
        )
    }
    val displayExpensesIr= _displayExpensesIr.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _currencySign = MutableStateFlow("")
    val currencySign  = _currencySign.asStateFlow()

    init {
        viewModelScope.launch {
            _finResults.value = useCases.getFinResults(
                flatId = filterState.value.selectedFlatId,
                year = filterState.value.yearMonth.year)
            val allFlats = coreUseCases.getAllFlats()
            _flatsState.value = flatsState.value.copy(
                listOfFlats = allFlats
            )
            _displayExpCategories.value = useCases.getExpCategories()
        }
    }

    fun homeScreenEvents(event: HomeScreenEvents){
        when(event) {

            is HomeScreenEvents.SendMessage -> {
                _messageState.value = messageState.value.copy(
                    send = true,
                    message = event.message
                )
            }

            is HomeScreenEvents.CloseMessage -> {
                _messageState.value = messageState.value.copy(
                    send = false,
                    message = R.string.err_msg_base_error
                )
            }


            is HomeScreenEvents.OnYearMonthChange -> {
                _filterState.value = filterState.value.copy(
                    yearMonth = event.yearMonth
                )
            }

            is HomeScreenEvents.OnNewFlatChanged -> {
                _flatsState.value = flatsState.value.copy(
                    newFlat = event.name
                )
            }
            is HomeScreenEvents.OnAddNewFlatBtnClick -> {
                viewModelScope.launch {
                    val status = useCases.addNewFlat(
                        flatsState.value.newFlat,
                        flatsState.value.listOfFlats
                    )
                    if(status== CheckStatusStr.SuccessStatus){
                        val newFlatList = coreUseCases.getAllFlats()
                        _flatsState.value = flatsState.value.copy(
                            newFlat = "",
                            listOfFlats = newFlatList
                        )
                    }
                    _messageState.value = messageState.value.copy(
                        send = true,
                        message = status.message
                    )

                }
            }

            is HomeScreenEvents.OnFlatClick -> {
                if(filterState.value.selectedFlatId!=event.id){
                    _filterState.value = filterState.value.copy(
                        selectedFlatId = event.id
                    )
                    viewModelScope.launch {
                        _finResults.value = useCases.getFinResults(
                            flatId = filterState.value.selectedFlatId,
                            year = filterState.value.yearMonth.year)
                    }
                }
            }

            //Home Screen State
            is HomeScreenEvents.OnPageChange -> {
                _homeScreenState.value = homeScreenState.value.copy(
                    page = event.page
                )
            }
            // Schedule Page
            is HomeScreenEvents.OnRentPaidChange -> {
                viewModelScope.launch {
                    val result = useCases.updatePaidStatus(id = event.id, isPaid = event.isPaid)
                    if(result != SimpleStatusOperation.OperationSuccess){
                        _messageState.value = messageState.value.copy(
                            send = true,
                            message = R.string.err_msg_unknown_error
                        )
                    }
                }
            }

            //Expenses Category
            is HomeScreenEvents.OnMoneyFlowChanged -> {
                _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                   isRegularMF = event.isRegularMF)
                _moneyFlowCategory.value = if(event.isRegularMF) MoneyFlowCategory.Regular else MoneyFlowCategory.Irregular



            }
            is HomeScreenEvents.OnYearChanged -> {
                _filterState.value = filterState.value.copy(
                    yearMonth = YearMonth.of(event.year, filterState.value.yearMonth.monthValue)
                )

            }
            is HomeScreenEvents.OnMonthClick -> {
                _filterState.value = filterState.value.copy(
                    yearMonth = YearMonth.of(filterState.value.yearMonth.year, event.monthInt)
                )
            }

            is HomeScreenEvents.OnNewNameExpCatChanged -> {
                _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                    newCategoryName = event.name
                )
            }
            is HomeScreenEvents.OnNewAmountExpCatChanged -> {
                _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                    newCategoryAmount = event.amount
                )
            }
            is HomeScreenEvents.OnAddNewExpCatBtnClick -> {

                viewModelScope.launch {
                    val status = useCases.addNewExpCat(
                        name = expensesCategoriesState.value.newCategoryName,
                        amount = expensesCategoriesState.value.newCategoryAmount,
                        categories = displayExpCategories.value.monthlyExpCategories,
                        moneyFlowCategory = moneyFlowCategory.value
                    )
                    if(status== CheckStatusStr.SuccessStatus){
                        _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                            newCategoryName = "",
                            newCategoryAmount = 0,
                        )
                        _displayExpCategories.value = useCases.getExpCategories()
                    }
                    _messageState.value = messageState.value.copy(
                        send = true,
                        message = status.message
                    )

                }
            }

            is HomeScreenEvents.OnAmountExpChanged -> {
                if(expensesCategoriesState.value.isRegularMF){
                    val newList = displayExpCategories.value.monthlyExpCategories.toMutableList()
                    newList[event.index] = newList[event.index].copy(
                        amount = event.amount
                    )
                    _displayExpCategories.value = displayExpCategories.value.copy(
                        monthlyExpCategories = newList
                    )
                } else {
                    val newList = displayExpCategories.value.irregularExpCategories.toMutableList()
                    newList[event.index] = newList[event.index].copy(
                        amount = event.amount
                    )
                    _displayExpCategories.value = displayExpCategories.value.copy(
                        irregularExpCategories = newList
                    )
                }

            }
            is HomeScreenEvents.OnSelectExpensesChange -> {
                _selectedExpenses.value = event.expensesInfo
            }

            is HomeScreenEvents.OnExpensesAmountChange -> {
                viewModelScope.launch {
                    val result = useCases.updateExpenses(
                        id = event.expensesId,
                        amount = event.amount
                    )

                    if(result != SimpleStatusOperation.OperationSuccess) {
                        _messageState.value = messageState.value.copy(
                            send = true,
                            message = R.string.err_msg_unknown_error
                        )
                    }



                }

            }
            is HomeScreenEvents.OnCategoriesChanged -> {
                viewModelScope.launch {
                    val result = useCases.updateCategories(
                        categories = event.listChangedCategories
                    )
                    if(result != SimpleStatusOperation.OperationSuccess){
                        _messageState.value = messageState.value.copy(
                            send = true,
                            message = R.string.err_msg_unknown_error
                        )
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
                        _messageState.value = messageState.value.copy(
                            send = true,
                            message = R.string.err_msg_unknown_error
                        )
                    }
                }

            }
            // New Booked Events
            is HomeScreenEvents.OnNameBookedChanged -> {
                _newBookedState.value = newBookedState.value.copy(
                    name = event.name
                )
            }
            is HomeScreenEvents.OnCommentBookedChanged -> {
                _newBookedState.value = newBookedState.value.copy(
                    comment = event.comment
                )
            }
            is HomeScreenEvents.SetCalendarState -> {
                viewModelScope.launch {
                    if(filterState.value.selectedFlatId == -1) {
                        _filterState.value = filterState.value.copy(
                            selectedFlatId = flatsState.value.listOfFlats[0].id
                        )
                    }
                    val bookedDays = useCases.getBookedDays(year=event.year, flatId = filterState.value.selectedFlatId)
                    val newStartDate = if((newBookedState.value.startDate?.year ?: 0) == event.year)
                        newBookedState.value.startDate else null
                    val newEndDate = if((newBookedState.value.endDate?.year ?: 0) == event.year)
                        newBookedState.value.endDate else null
                    _calendarState.value = CalendarState(
                        year = event.year,
                        bookedDays = bookedDays,
                        startDate = newStartDate,
                        endDate = newEndDate
                    )
                }

            }

            is HomeScreenEvents.OnDateRangeChanged -> {
                val nights = if(event.startDate!=null && event.endDate!=null)
                    ChronoUnit.DAYS.between(event.startDate, event.endDate).toInt() else 0

                _newBookedState.value = newBookedState.value.copy(
                    startDate = event.startDate,
                    endDate = event.endDate,
                    nights = nights,
                    allMoney = nights*newBookedState.value.oneNightMoney
                )

            }
            is HomeScreenEvents.OnOneNightMoneyChange -> {
                _newBookedState.value = newBookedState.value.copy(
                    oneNightMoney = event.money,
                    allMoney = if(newBookedState.value.nights!=0) event.money*newBookedState.value.nights else
                        event.money
                )
            }
            is HomeScreenEvents.OnAllMoneyChange -> {
                _newBookedState.value = newBookedState.value.copy(
                    oneNightMoney =  if(newBookedState.value.nights!=0) event.money/newBookedState.value.nights else
                        event.money,
                    allMoney = event.money,
                )
            }
            is HomeScreenEvents.OnAddNewBookedBtnClick -> {
                viewModelScope.launch {
                    val result = useCases.addNewBooked(
                        newBookedState = newBookedState.value,
                        flatId = filterState.value.selectedFlatId
                    )
                    if (result==CheckStatusBooked.SuccessStatus){
                        _newBookedState.value = newBookedState.value.copy(
                            startDate = null,
                            endDate = null,
                            name = "",
                            comment = "",
                            nights = 0,
                            oneNightMoney = 0,
                            allMoney = 0
                        )
                    }
                    _messageState.value = messageState.value.copy(
                        send = true,
                        message = result.message
                    )
                }
            }

            //Currency dialog
            is HomeScreenEvents.OnCurrencySignChanged -> {
                _currencySign.value = event.sign
            }
        }
    }
}