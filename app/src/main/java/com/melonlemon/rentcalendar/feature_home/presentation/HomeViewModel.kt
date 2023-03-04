package com.melonlemon.rentcalendar.feature_home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.model.FinResultsDisplay
import com.melonlemon.rentcalendar.feature_home.domain.model.RentInfo
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.HomeUseCases
import com.melonlemon.rentcalendar.feature_home.presentation.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val useCases: HomeUseCases
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _rentList = filterState.flatMapLatest{ filterState ->
        useCases.getRentList(yearMonth = filterState.yearMonth, flatId = filterState.selectedFlatId)
    }

    val rentList  = _rentList.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList<RentInfo>()
    )


    private val _moneyFlowCategory = MutableStateFlow(MoneyFlowCategory.RegularFixed)
    val moneyFlowCategory  = _moneyFlowCategory.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _displayExpCategories = moneyFlowCategory.flatMapLatest{ moneyFlowCategory ->
        useCases.getExpCategories(moneyFlowCategory)
    }
    val displayExpCategories  = _displayExpCategories.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList<ExpensesCategoryInfo>()
    )

    init {
        viewModelScope.launch {
            _finResults.value = useCases.getFinResults(filterState.value.selectedFlatId)

        }
    }

    fun homeScreenEvents(event: HomeScreenEvents){
        when(event) {
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
                        val newFlatList = useCases.getAllFlats()
                        _flatsState.value = flatsState.value.copy(
                            newFlat = "",
                            listOfFlats = newFlatList,
                            checkStatusNewFlat = status
                        )
                    } else {
                        _flatsState.value = flatsState.value.copy(
                            checkStatusNewFlat = status
                        )
                    }
                }
            }
            is HomeScreenEvents.OnAddNewFlatComplete -> {
                _flatsState.value = flatsState.value.copy(
                   checkStatusNewFlat = CheckStatusStr.UnCheckedStatus
                )
            }
            is HomeScreenEvents.OnFlatClick -> {
                _filterState.value = filterState.value.copy(
                    selectedFlatId = event.id
                )
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
                    if(result == SimpleStatusOperation.OperationSuccess){

                    } else {

                    }
                }
            }

            //Expenses Category
            is HomeScreenEvents.OnMoneyFlowChanged -> {
                _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                   isRegularMF = event.isRegularMF,
                    isFixedMF = event.isFixedMF
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
                        categories = displayExpCategories.value,
                        moneyFlowCategory = moneyFlowCategory.value
                    )
                    if(status== CheckStatusStr.SuccessStatus){
                        _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                            newCategoryName = "",
                            newCategoryAmount = 0,
                            checkStatusNewCat = status
                        )
                    } else {
                        _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                            checkStatusNewCat = status
                        )
                    }
                }
            }
            is HomeScreenEvents.OnAddNewExpCatComplete -> {
                _expensesCategoriesState.value = expensesCategoriesState.value.copy(
                    checkStatusNewCat = CheckStatusStr.UnCheckedStatus
                )
            }
            is HomeScreenEvents.OnAmountExpChanged -> {
                
            }
            is HomeScreenEvents.OnExpensesAdd -> {

            }
            // New Booked Events
            is HomeScreenEvents.OnCalendarBtnClick -> {

            }
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
                    val result = useCases.addNewBooked(newBookedState.value)
                    if (result==CheckStatusBooked.SuccessStatus){
                        _newBookedState.value = newBookedState.value.copy(
                            startDate = null,
                            endDate = null,
                            name = "",
                            comment = "",
                            nights = 0,
                            oneNightMoney = 0,
                            allMoney = 0,
                            checkStatusNewBooked = result
                        )
                    } else {
                        _newBookedState.value = newBookedState.value.copy(
                            checkStatusNewBooked = result
                        )
                    }
                }

            }
            is HomeScreenEvents.OnAddNewBookedComplete -> {
                _newBookedState.value = newBookedState.value.copy(
                    checkStatusNewBooked = CheckStatusBooked.UnCheckedStatus
                )
            }

        }
    }
}