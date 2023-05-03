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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.presentation.components.*
import com.melonlemon.rentcalendar.feature_home.domain.model.ExpensesInfo
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.*
import com.melonlemon.rentcalendar.feature_home.presentation.components.*
import com.melonlemon.rentcalendar.feature_home.presentation.util.*
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {

    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val dependState by viewModel.dependState.collectAsStateWithLifecycle()
    val independentState by viewModel.independentState.collectAsStateWithLifecycle()
    val messageState by viewModel.messageState.collectAsStateWithLifecycle()


    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    if(messageState.send){
        LaunchedEffect(messageState.message){
            snackbarHostState.showSnackbar(
                message = context.resources.getString(messageState.message),
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.CloseMessage)
        }
    }

    val currencyDialog = remember{ mutableStateOf(false) }
    val changeExpensesDialog = remember{ mutableStateOf(false)}
    val allExpCategories = remember{ mutableStateOf(false) }
    val calendarDialog = remember{ mutableStateOf(false) }

    val homeScreenState = remember{ mutableStateOf<HomePages>(HomePages.SchedulePage) }


    val monthlyIrregularToggle = remember{ mutableStateOf(true) }

    var listExpenses = remember { derivedStateOf {
        if(monthlyIrregularToggle.value) dependState.expensesPageState.monthlyExpenses else
            dependState.expensesPageState.irregularExpenses
    }

    }

    val selectedExpenses = remember{ mutableStateOf(
        ExpensesInfo(
        id = -1,
        categoryId = -1,
        categoryName = "",
        paymentDate = LocalDate.now(),
        amount = 0)
    ) }

    val allFlatsName = stringResource(R.string.all_flats)

    val flatName = remember { derivedStateOf {
        if(filterState.selectedFlatId==-1 || independentState.flatState.listOfFlats.isEmpty()){
            allFlatsName
        }else {
            independentState.flatState.listOfFlats.filter { it.id == filterState.selectedFlatId }[0].name
        }

    } }


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
                        items = dependState.finResultsDisplay,
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
                            currencySign = independentState.currencySign
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
                        name = independentState.flatState.newFlat,
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
                        items = independentState.flatState.listOfFlats,
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
                            isSelected = homeScreenState.value == HomePages.SchedulePage,
                            onBtnClick = {
                                homeScreenState.value = HomePages.SchedulePage
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    item {
                        SectionButton(
                            text = stringResource(R.string.expenses),
                            isSelected = homeScreenState.value == HomePages.ExpensesPage,
                            onBtnClick = {
                                homeScreenState.value = HomePages.ExpensesPage
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    item {
                        SectionButton(
                            text = stringResource(R.string.book),
                            isSelected = homeScreenState.value == HomePages.BookPage,
                            onBtnClick = {
                                homeScreenState.value = HomePages.BookPage
                            }
                        )
                    }

                }
            }

            //SCHEDULE PAGE
            if (homeScreenState.value == HomePages.SchedulePage) {
                items(
                    items = dependState.schedulePageState.rentInfo,
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

            //EXPENSES PAGE

            if (homeScreenState.value == HomePages.ExpensesPage) {
                // Monthly/Irregular BTN Choose +  VIEW ALL CATEGORIES BTN
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SegmentedTwoBtns(
                            firstBtnName = stringResource(R.string.regular_btn),
                            secondBtnName = stringResource(R.string.irregular_btn),
                            isFirstBtnSelected = monthlyIrregularToggle.value,
                            onBtnClick = { isMonthlySelected ->
                                monthlyIrregularToggle.value = isMonthlySelected
                            }
                        )
                        IconButton(onClick = {
                            allExpCategories.value = true
                        }) {
                            Icon(imageVector = Icons.Default.List, contentDescription = null)
                        }

                    }
                }

                //ADD NEW CATEGORIES
                item {
                    InputContainer(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_create_new_folder_24),
                        title = stringResource(R.string.new_category)
                    ) {
                        NameValueInputPlus(
                            name = independentState.expCategoriesState.newCategoryName,
                            number = independentState.expCategoriesState.newCategoryAmount,
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
                                    HomeScreenEvents.OnAddNewExpCatBtnClick(
                                        if(monthlyIrregularToggle.value) MoneyFlowCategory.Regular
                                    else MoneyFlowCategory.Irregular
                                    )
                                )
                            }
                        )
                    }
                }
                item {
                    Text(text = stringResource(R.string.title_exp_cat))
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
                }


                //NO CATEGORIES - SHOW MESSAGE
                if((monthlyIrregularToggle.value && independentState.expCategoriesState.monthlyExpCategories.isEmpty()) ||
                    (!monthlyIrregularToggle.value && independentState.expCategoriesState.irregularExpCategories.isEmpty())
                ){
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
                }

                //MONTHLY CATEGORY EXPENSES
                if(monthlyIrregularToggle.value && independentState.expCategoriesState.monthlyExpCategories.isNotEmpty()){

                    itemsIndexed(
                        items = independentState.expCategoriesState.monthlyExpCategories,
                        key = { _, category ->
                            "M100" + category.id
                        }
                    ) { index, category ->
                        //if categoryId in Expenses Transactions do not show
                        //Monthly categories can have only 1 payment for month
                        if (category.id !in dependState.expensesPageState.monthlyExpenses.map { it.categoryId }) {
                            InputInfoCard(
                                modifier = Modifier.fillMaxWidth(),
                                textFirstR = category.name,
                                textSecondR = { Text(category.subHeader) },
                                inputNumber = if (category.amount != 0) category.amount.toString() else "",
                                onNumberChanged = { numberString ->
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnAmountExpChanged(
                                            index = index,
                                            amount = numberString.toIntOrNull() ?: 0,
                                            monthlyIrregularToggle = true
                                        )
                                    )
                                },
                                onAddButtonClicked = {
                                    if(filterState.selectedFlatId!=-1){
                                        viewModel.homeScreenEvents(
                                            HomeScreenEvents.OnExpensesAdd(
                                                catId = category.id,
                                                amount = category.amount,
                                                categoryName = category.name
                                            ))
                                    } else {
                                        viewModel.homeScreenEvents(
                                            HomeScreenEvents.SendMessage(
                                                message = R.string.err_msg_choose_flat
                                            )
                                        )
                                    }

                                },
                                content = {}
                            )
                        }

                    }
                }

                //IRREGULAR CATEGORY EXPENSES
                if(!monthlyIrregularToggle.value && independentState.expCategoriesState.irregularExpCategories.isNotEmpty()){

                    itemsIndexed(
                        items = independentState.expCategoriesState.irregularExpCategories,
                        key = { _, category ->
                            "IRR100" +  category.id
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
                                        amount = numberString.toIntOrNull() ?: 0,
                                        monthlyIrregularToggle = false
                                    )
                                )
                            },
                            onAddButtonClicked = {
                                if(filterState.selectedFlatId!=-1){
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnExpensesAdd(
                                            catId = category.id,
                                            amount = category.amount,
                                            categoryName = category.name
                                        ))
                                } else {
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.SendMessage(
                                            message = R.string.err_msg_choose_flat
                                        )
                                    )

                                }

                            },
                            content = {}
                        )
                    }
                }


                item {
                    Text(text = stringResource(R.string.expenses))
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
                }

                //LIST EXPENSES

                items(
                    items = listExpenses.value,
                    key = { expenses ->
                        "EXP200" + expenses.id
                    }
                ) { item ->
                    DisplayInfoCard(
                        textFirstR = item.categoryName,
                        textSecondR = "${item.paymentDate} - ${item.amount}",
                        onDoubleTap = {
                            selectedExpenses.value = item
                            changeExpensesDialog.value = true
                        }
                    )
                }
            }

            //BOOKING PAGE
            if (homeScreenState.value == HomePages.BookPage) {
                item {
                    DateRange(
                        startDate = independentState.newBookedState.startDate,
                        endDate = independentState.newBookedState.endDate,
                        onCalendarBtnClick = {
                            viewModel.homeScreenEvents(HomeScreenEvents.SetCalendarState(filterState.yearMonth.year))
                            calendarDialog.value = true
                        }
                    )
                }
                item {
                    NameCommentFields(
                        name = independentState.newBookedState.name,
                        comment = independentState.newBookedState.comment,
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
                        nights = independentState.newBookedState.nights,
                        oneNightMoney = independentState.newBookedState.oneNightMoney,
                        allMoney = independentState.newBookedState.allMoney,
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
                            if(filterState.selectedFlatId!=-1){
                                viewModel.homeScreenEvents(
                                    HomeScreenEvents.OnAddNewBookedBtnClick
                                )
                            } else {
                                viewModel.homeScreenEvents(
                                    HomeScreenEvents.SendMessage(
                                        message = R.string.err_msg_choose_flat
                                    )
                                )

                            }

                        }
                    )
                }

            }
        }

        if(currencyDialog.value){
            CurrencyDialog(
                currencySign = independentState.currencySign,
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
                regularCategories = independentState.expCategoriesState.monthlyExpCategories,
                irregularCategories = independentState.expCategoriesState.irregularExpCategories,
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
                            expensesId = selectedExpenses.value.id,
                            amount = amount
                        )
                    )
                    changeExpensesDialog.value = false
                },
                name = selectedExpenses.value.categoryName,
                paymentDate = selectedExpenses.value.paymentDate,
                amount = selectedExpenses.value.amount
            )
        }

        if(calendarDialog.value){
            CustomCalendarDialog(
                flatName = flatName.value,
                startDate = independentState.calendarState.startDate,
                endDate = independentState.calendarState.endDate,
                cellSize = Size(48f, 48f),
                onCancel = {
                    calendarDialog.value = false
                },
                year = independentState.calendarState.year,
                onYearChanged = { year ->
                    viewModel.homeScreenEvents(HomeScreenEvents.SetCalendarState(year))
                },
                bookedDays = independentState.calendarState.bookedDays,
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



