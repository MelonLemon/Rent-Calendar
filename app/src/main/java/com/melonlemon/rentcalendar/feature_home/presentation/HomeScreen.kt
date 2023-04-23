package com.melonlemon.rentcalendar.feature_home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.*
import com.melonlemon.rentcalendar.core.presentation.util.SimpleStatusOperation
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesCategoryInfo
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.*
import com.melonlemon.rentcalendar.feature_home.presentation.components.*
import com.melonlemon.rentcalendar.feature_home.presentation.util.*


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val finResults by viewModel.finResults.collectAsStateWithLifecycle()
    val flatsState by viewModel.flatsState.collectAsStateWithLifecycle()
    val homeScreenState by viewModel.homeScreenState.collectAsStateWithLifecycle()
    val rentList by viewModel.rentList.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val expensesCategoriesState by viewModel.expensesCategoriesState.collectAsStateWithLifecycle()
    val displayExpCategories by viewModel.displayExpCategories.collectAsStateWithLifecycle()
    val newBookedState by viewModel.newBookedState.collectAsStateWithLifecycle()
    val currencySign by viewModel.currencySign.collectAsStateWithLifecycle()
    val displayExpensesReg by viewModel.displayExpensesReg.collectAsStateWithLifecycle()
    val displayExpensesIr by viewModel.displayExpensesIr.collectAsStateWithLifecycle()
    val selectedExpenses by viewModel.selectedExpenses.collectAsStateWithLifecycle()
    val updateExpensesStatus by viewModel.updateExpensesStatus.collectAsStateWithLifecycle()
    val calendarState by viewModel.calendarState.collectAsStateWithLifecycle()
    val failAttempt by viewModel.failAttempt.collectAsStateWithLifecycle()


    val snackbarHostState = remember { SnackbarHostState() }
    val errorStatus = stringResource(R.string.err_msg_unknown_error)
    val successStatus = stringResource(R.string.msg_success_status)

    if(flatsState.checkStatusNewFlat!= CheckStatusStr.UnCheckedStatus){
        val message = stringResource(flatsState.checkStatusNewFlat.message)
        LaunchedEffect(flatsState.checkStatusNewFlat){
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.OnAddNewExpCatComplete)
        }
    }

    if(failAttempt){
        LaunchedEffect(failAttempt){
            snackbarHostState.showSnackbar(
                message = errorStatus,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.RefreshFailAttempt)
        }
    }

    if(expensesCategoriesState.checkStatusNewCat!= CheckStatusStr.UnCheckedStatus){
        val message = stringResource(flatsState.checkStatusNewFlat.message)
        LaunchedEffect(expensesCategoriesState.checkStatusNewCat){
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.OnAddNewFlatComplete)
        }
    }

    if(newBookedState.checkStatusNewBooked!= CheckStatusBooked.UnCheckedStatus){
        val message = stringResource(newBookedState.checkStatusNewBooked.message)
        LaunchedEffect(newBookedState.checkStatusNewBooked){
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.OnAddNewFlatComplete)
        }
    }

    if(updateExpensesStatus != SimpleStatusOperation.OperationUnChecked){

        LaunchedEffect(updateExpensesStatus){

            snackbarHostState.showSnackbar(
                message = if(updateExpensesStatus==SimpleStatusOperation.OperationSuccess) successStatus else errorStatus,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.OnExpensesUpdateComplete)
        }
    }

    val currencyDialog = remember{ mutableStateOf(false) }
    val changeExpensesDialog = remember{ mutableStateOf(false)}
    val allExpCategories = remember{ mutableStateOf(false) }
    val calendarDialog = remember{ mutableStateOf(false) }

    val flatName = remember { derivedStateOf { flatsState.listOfFlats.filter { it.id == filterState.selectedFlatId }[0].name } }



    val listExpenses = remember(expensesCategoriesState.isRegularMF) {
        if (expensesCategoriesState.isRegularMF) displayExpensesReg else displayExpensesIr
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { it ->

        LazyColumn(
            modifier = Modifier
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            item {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = finResults,
                        key = { monthResults ->
                            monthResults.yearMonth
                        }
                    ) { monthResults ->
                        FinanceResultWidget(
                            modifier = Modifier.fillParentMaxWidth(),
                            flatName = if (filterState.selectedFlatId == -1) stringResource(R.string.all)
                            else flatName.value,
                            month = monthResults.yearMonth,
                            bookedPercent = monthResults.percentBooked,
                            income = monthResults.income,
                            expenses = monthResults.expenses,
                            currencySign = currencySign
                        )
                    }

                }
            }
            item {
                Button(onClick = {
                    currencyDialog.value = true
                }) {
                    Text(text = stringResource(R.string.currency_sign))
                }
            }

            item {
                InputContainer(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_create_new_folder_24),
                    title = stringResource(R.string.new_flat)
                ) {
                    NameInputPlus(
                        name = flatsState.newFlat,
                        onNameChanged = { name ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnNewFlatChanged(name)
                            )
                        },
                        onAddButtonClicked = {
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnAddNewFlatBtnClick
                            )
                        }
                    )
                }
            }

            item {
                LazyRow(
                    modifier = Modifier,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    item {
                        FilterButton(
                            text = stringResource(R.string.all),
                            isSelected = filterState.selectedFlatId == -1,
                            onBtnClick = {
                                viewModel.homeScreenEvents(HomeScreenEvents.OnFlatClick(-1))
                            },
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    items(
                        items = flatsState.listOfFlats,
                        key = { item ->
                            item.id
                        }
                    ) { item ->
                        FilterButton(
                            text = item.name,
                            isSelected = filterState.selectedFlatId == item.id,
                            onBtnClick = {
                                viewModel.homeScreenEvents(HomeScreenEvents.OnFlatClick(item.id))
                            },
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            item {
                YearMonthRow(
                    modifier = Modifier.fillMaxWidth(),
                    yearMonth = filterState.yearMonth,
                    onYearChange = { yearString ->
                        viewModel.homeScreenEvents(
                            HomeScreenEvents.OnYearChanged(
                                year = yearString.toIntOrNull() ?: 0
                            )
                        )
                    },
                    onMonthClick = { monthInt ->
                        viewModel.homeScreenEvents(
                            HomeScreenEvents.OnMonthClick(
                                monthInt = monthInt
                            )
                        )
                    }
                )
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    item {
                        SectionButton(
                            text = stringResource(R.string.schedule),
                            isSelected = homeScreenState.page == HomePages.SchedulePage,
                            onBtnClick = {
                                viewModel.homeScreenEvents(HomeScreenEvents.OnPageChange(HomePages.SchedulePage))
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    item {
                        SectionButton(
                            text = stringResource(R.string.expenses),
                            isSelected = homeScreenState.page == HomePages.ExpensesPage,
                            onBtnClick = {
                                viewModel.homeScreenEvents(HomeScreenEvents.OnPageChange(HomePages.ExpensesPage))
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    item {
                        SectionButton(
                            text = stringResource(R.string.book),
                            isSelected = homeScreenState.page == HomePages.BookPage,
                            onBtnClick = {
                                viewModel.homeScreenEvents(HomeScreenEvents.OnPageChange(HomePages.BookPage))
                            }
                        )
                    }

                }
            }
            if (homeScreenState.page == HomePages.SchedulePage) {
                items(
                    items = rentList,
                    key = { rent ->
                        rent.id
                    }
                ) { rent ->
                    RentCard(
                        name = rent.name,
                        description = rent.description,
                        periodStart = rent.periodStart,
                        periodEnd = rent.periodEnd,
                        amount = rent.amount,
                        isPaid = rent.isPaid,
                        onPaidChange = { isPaid ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnRentPaidChange(
                                    id = rent.id,
                                    isPaid = isPaid
                                )
                            )
                        }
                    )
                }
            }
            if (homeScreenState.page == HomePages.ExpensesPage) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SegmentedTwoBtns(
                            firstBtnName = stringResource(R.string.regular_btn),
                            secondBtnName = stringResource(R.string.irregular_btn),
                            isFirstBtnSelected = expensesCategoriesState.isRegularMF,
                            onBtnClick = { isRegularSelected ->
                                viewModel.homeScreenEvents(
                                    HomeScreenEvents.OnMoneyFlowChanged(
                                        isRegularMF = isRegularSelected
                                    )
                                )
                            }
                        )
                        IconButton(onClick = {
                            allExpCategories.value = true
                        }) {
                            Icon(imageVector = Icons.Default.List, contentDescription = null)
                        }

                    }

                }
                item {
                    InputContainer(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_create_new_folder_24),
                        title = stringResource(R.string.new_category)
                    ) {
                        NameValueInputPlus(
                            name = expensesCategoriesState.newCategoryName,
                            number = expensesCategoriesState.newCategoryAmount,
                            onNumberChanged = { amount ->
                                viewModel.homeScreenEvents(
                                    HomeScreenEvents.OnNewAmountExpCatChanged(
                                        amount.toIntOrNull() ?: 0
                                    )
                                )
                            },
                            onNameChanged = { name ->
                                viewModel.homeScreenEvents(
                                    HomeScreenEvents.OnNewNameExpCatChanged(name)
                                )
                            },
                            onAddButtonClicked = {
                                viewModel.homeScreenEvents(
                                    HomeScreenEvents.OnAddNewExpCatBtnClick
                                )
                            }
                        )
                    }
                }
                item {
                    Text(text = stringResource(R.string.title_exp_cat))
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
                }



                if (expensesCategoriesState.isRegularMF) {
                    if (displayExpCategories.first.isEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.no_categories),
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                            Image(
                                bitmap = ImageBitmap.imageResource(id = R.drawable.categories),
                                contentDescription = null)
                        }
                    } else {
                        itemsIndexed(
                            items = displayExpCategories.first,
                            key = { _, category ->
                                category.id
                            }
                        ) { index, category ->
                            //if categoryId in Expenses Transactions do not show
                            //Monthly categories can have only 1 payment for month
                            if (category.id !in displayExpensesReg.map { it.categoryId }) {
                                InputInfoCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    textFirstR = category.name,
                                    textSecondR = { Text(category.subHeader) },
                                    inputNumber = if (category.amount != 0) category.amount.toString() else "",
                                    onNumberChanged = { numberString ->
                                        viewModel.homeScreenEvents(
                                            HomeScreenEvents.OnAmountExpChanged(
                                                index = index,
                                                amount = numberString.toIntOrNull() ?: 0
                                            )
                                        )
                                    },
                                    onAddButtonClicked = {
                                        viewModel.homeScreenEvents(
                                            HomeScreenEvents.OnExpensesAdd(
                                                catId = category.id,
                                                amount = category.amount,
                                                categoryName = category.name
                                            ))
                                    },
                                    content = {}
                                )
                            }

                        }
                    }
                } else {
                    if (displayExpCategories.second.isEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.no_categories),
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                            Image(
                                bitmap = ImageBitmap.imageResource(id = R.drawable.categories),
                                contentDescription = null)
                        }
                    } else {
                        itemsIndexed(
                            items = displayExpCategories.second,
                            key = { _, category ->
                                category.id
                            }
                        ) { index, category ->
                            InputInfoCard(
                                modifier = Modifier.fillMaxWidth(),
                                textFirstR = category.name,
                                textSecondR = { Text(category.subHeader) },
                                inputNumber = if (category.amount != 0) category.amount.toString() else "",
                                onNumberChanged = { numberString ->
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnAmountExpChanged(
                                            index = index,
                                            amount = numberString.toIntOrNull() ?: 0
                                        )
                                    )
                                },
                                onAddButtonClicked = {
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnExpensesAdd(
                                            catId = category.id,
                                            amount = category.amount,
                                            categoryName = category.name
                                        )
                                    )

                                },
                                content = {}
                            )
                        }
                    }
                }

                item {
                    Text(text = stringResource(R.string.expenses))
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
                }



                items(
                    items = listExpenses,
                    key = { expenses ->
                        expenses.id
                    }
                ) { item ->
                    DisplayInfoCard(
                        textFirstR = item.categoryName,
                        textSecondR = "${item.paymentDate} - ${item.amount}",
                        onDoubleTap = {
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnSelectExpensesChange(
                                    expensesInfo = item
                                )
                            )
                            changeExpensesDialog.value = true
                        }
                    )
                }


            }
            if (homeScreenState.page == HomePages.BookPage) {
                item {
                    DateRange(
                        startDate = newBookedState.startDate,
                        endDate = newBookedState.endDate,
                        onCalendarBtnClick = {
                            calendarDialog.value = true
                        }
                    )
                }
                item {
                    NameCommentFields(
                        name = newBookedState.name,
                        comment = newBookedState.comment,
                        onNameChange = { name ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnNameBookedChanged(name)
                            )
                        },
                        onCommentChange = { comment ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnCommentBookedChanged(comment)
                            )
                        }
                    )
                }
                item {
                    PaymentWidget(
                        nights = newBookedState.nights,
                        oneNightMoney = newBookedState.oneNightMoney,
                        allMoney = newBookedState.allMoney,
                        onAllMoneyChange = { moneyString ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnAllMoneyChange(
                                    moneyString.toIntOrNull() ?: 0
                                )
                            )
                        },
                        onNightMChange = { moneyString ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnOneNightMoneyChange(
                                    moneyString.toIntOrNull() ?: 0
                                )
                            )
                        }
                    )
                }
                item {
                    SectionButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.add_btn),
                        isSelected = true,
                        onBtnClick = {
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnAddNewBookedBtnClick
                            )
                        }
                    )
                }

            }
        }

        if(currencyDialog.value){
            CurrencyDialog(
                currencySign = currencySign,
                onCancel = {
                    currencyDialog.value = false
                },
                onSave = { sign ->
                    viewModel.homeScreenEvents(
                        HomeScreenEvents.OnCurrencySignChanged(sign))
                    currencyDialog.value = false
                }
            )
        }

        if(allExpCategories.value){
            AllExpCategoriesDialog(
                regularCategories = displayExpCategories.second,
                irregularCategories = displayExpCategories.second,
                onCancel = {
                    allExpCategories.value = false
                },
                onAgree = { listCategoriesChanged ->
                    viewModel.homeScreenEvents(
                        HomeScreenEvents.OnCategoriesChanged(listCategoriesChanged)
                    )
                    allExpCategories.value = false
                }
            )
        }

        if(changeExpensesDialog.value){
            ExpensesDialog(
                onCancel = {
                    changeExpensesDialog.value = false
                },
                onAgree = { amount ->
                    viewModel.homeScreenEvents(
                        HomeScreenEvents.OnExpensesAmountChange(
                            expensesId = selectedExpenses.id,
                            amount = amount
                        )
                    )
                    changeExpensesDialog.value = false
                },
                name = selectedExpenses.categoryName,
                paymentDate = selectedExpenses.paymentDate,
                amount = selectedExpenses.amount
            )
        }

        if(calendarDialog.value){
            CustomCalendarDialog(
                flatName = flatName.value,
                startDate = calendarState.startDate,
                endDate = calendarState.endDate,
                cellSize = Size(48f, 48f),
                onCancel = {
                    calendarDialog.value = false
                },
                year = calendarState.year,
                onYearChanged = { year ->
                    viewModel.homeScreenEvents(HomeScreenEvents.SetCalendarState(year))
                },
                bookedDays = calendarState.bookedDays,
                onSave = { startDate, endDate ->
                    viewModel.homeScreenEvents(HomeScreenEvents.OnDateRangeChanged(
                        startDate = startDate,
                        endDate = endDate
                    ))
                    calendarDialog.value = false
                }
            )
        }


    }

}



