package com.melonlemon.rentcalendar.feature_home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.data.repository.HomeRepositoryImpl
import com.melonlemon.rentcalendar.core.presentation.components.*
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.*
import com.melonlemon.rentcalendar.feature_home.presentation.components.FinanceResultWidget
import com.melonlemon.rentcalendar.feature_home.presentation.components.PaymentWidget
import com.melonlemon.rentcalendar.feature_home.presentation.components.RentCard
import com.melonlemon.rentcalendar.feature_home.presentation.util.*
import com.melonlemon.rentcalendar.ui.theme.RentCalendarTheme
import java.time.YearMonth

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

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessages = mapOf(
        CheckStatusStr.BlankFailStatus to stringResource(R.string.err_msg_empty_flat),
        CheckStatusStr.DuplicateFailStatus to stringResource(R.string.err_msg_duplicate_name),
        CheckStatusStr.SuccessStatus to stringResource(R.string.msg_success_status),
        CheckStatusStr.UnKnownFailStatus to  stringResource(R.string.err_msg_unknown_error)
    )
    val errorMessagesBooked = mapOf(
        CheckStatusBooked.BlankDatesFailStatus to stringResource(R.string.booked_err_msg_dates),
        CheckStatusBooked.BlankNameFailStatus to stringResource(R.string.booked_err_msg_name),
        CheckStatusBooked.BlankPaymentFailStatus to stringResource(R.string.booked_err_msg_pay),
        CheckStatusBooked.SuccessStatus to stringResource(R.string.msg_success_status),
        CheckStatusBooked.UnKnownFailStatus to stringResource(R.string.err_msg_unknown_error),
    )
    val errorStatus = stringResource(R.string.err_msg_unknown_error)

    if(flatsState.checkStatusNewFlat!= CheckStatusStr.UnCheckedStatus){

        LaunchedEffect(flatsState.checkStatusNewFlat){

            snackbarHostState.showSnackbar(
                message = errorMessages[flatsState.checkStatusNewFlat]?: errorStatus,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.OnAddNewExpCatComplete)
        }
    }

    if(expensesCategoriesState.checkStatusNewCat!= CheckStatusStr.UnCheckedStatus){

        LaunchedEffect(expensesCategoriesState.checkStatusNewCat){

            snackbarHostState.showSnackbar(
                message = errorMessages[expensesCategoriesState.checkStatusNewCat]?: errorStatus,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.OnAddNewFlatComplete)
        }
    }

    if(newBookedState.checkStatusNewBooked!= CheckStatusBooked.UnCheckedStatus){

        LaunchedEffect(newBookedState.checkStatusNewBooked){

            snackbarHostState.showSnackbar(
                message = errorMessagesBooked[newBookedState.checkStatusNewBooked]?: errorStatus,
                actionLabel = null
            )
            viewModel.homeScreenEvents(HomeScreenEvents.OnAddNewFlatComplete)
        }
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
        ){
            item{
                LazyRow(){
                    itemsIndexed(finResults){ index, monthResults ->
                        FinanceResultWidget(
                            flatName=monthResults.flatName,
                            month= monthResults.yearMonth,
                            bookedPercent=monthResults.percentBooked,
                            income=monthResults.income,
                            expenses=monthResults.expenses,
                            currencySign = "$" // currency sign?
                        )
                    }

                }
            }

            item {
                InputContainer(
                  icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_create_new_folder_24),
                    title = stringResource(R.string.new_flat)
                ){
                    NameInputPlus(
                        name = flatsState.newFlat,
                        onNameChanged = { name ->
                                 viewModel.homeScreenEvents(
                                     HomeScreenEvents.OnNewFlatChanged(name))
                        },
                        onAddButtonClicked = {
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnAddNewFlatBtnClick)
                        }
                    )
                }
            }

            item{
                LazyRow(
                    modifier = Modifier,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ){
                    item{
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
            val currentYearMonth = YearMonth.now()
            item{
                Text(
                    text = if(filterState.yearMonth == currentYearMonth)
                        stringResource(R.string.this_month) + ": ${filterState.yearMonth.month.name} ${filterState.yearMonth.year}"
                else "${filterState.yearMonth.month.name}${filterState.yearMonth.year}",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    SectionButton(
                        text = stringResource(R.string.schedule),
                        isSelected = homeScreenState.page == HomePages.SchedulePage,
                        onBtnClick = {
                            viewModel.homeScreenEvents(HomeScreenEvents.OnPageChange(HomePages.SchedulePage))
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    SectionButton(
                        text = stringResource(R.string.expenses),
                        isSelected = homeScreenState.page == HomePages.ExpensesPage,
                        onBtnClick = {
                            viewModel.homeScreenEvents(HomeScreenEvents.OnPageChange(HomePages.ExpensesPage))
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    SectionButton(
                        text = stringResource(R.string.book),
                        isSelected = homeScreenState.page == HomePages.BookPage,
                        onBtnClick = {
                            viewModel.homeScreenEvents(HomeScreenEvents.OnPageChange(HomePages.BookPage))
                        }
                    )
                }
            }
            if(homeScreenState.page == HomePages.SchedulePage){
                itemsIndexed(rentList){ index, rent ->
                    RentCard(
                        name=rent.name,
                        description=rent.description,
                        periodStart=rent.periodStart,
                        periodEnd= rent.periodEnd,
                        amount=rent.amount,
                        isPaid = rent.isPaid,
                        onPaidChange = { isPaid ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnRentPaidChange(
                                    id = rent.id,
                                    isPaid = isPaid
                                ))
                        }
                    )
                }
            }
            if(homeScreenState.page == HomePages.ExpensesPage){
                item {
                    SegmentedTwoBtns(
                        firstBtnName = stringResource(R.string.regular_btn),
                        secondBtnName = stringResource(R.string.irregular_btn),
                        isFirstBtnSelected = expensesCategoriesState.isRegularMF,
                        onBtnClick = { isRegularSelected ->
                            viewModel.homeScreenEvents(HomeScreenEvents.OnMoneyFlowChanged(
                                isRegularMF = isRegularSelected,
                                isFixedMF = expensesCategoriesState.isFixedMF
                            ))
                        }
                    )
                    Text(
                        text= stringResource(R.string.amount),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    SegmentedTwoBtns(
                        firstBtnName = stringResource(R.string.fixed_amount),
                        secondBtnName = stringResource(R.string.variable_amount),
                        isFirstBtnSelected = expensesCategoriesState.isFixedMF,
                        onBtnClick = { isFixedAmountSelected ->
                            viewModel.homeScreenEvents(HomeScreenEvents.OnMoneyFlowChanged(
                                isRegularMF = expensesCategoriesState.isRegularMF,
                                isFixedMF = isFixedAmountSelected
                            ))
                        }
                    )
                }
                item{
                    InputContainer(
                        icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_create_new_folder_24),
                        title = stringResource(R.string.new_category)
                    ){
                        if(expensesCategoriesState.isFixedMF){
                            NameValueInputPlus(
                                name = expensesCategoriesState.newCategoryName,
                                number = expensesCategoriesState.newCategoryAmount,
                                onNumberChanged = { amount ->
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnNewAmountExpCatChanged(
                                            amount.toIntOrNull() ?:0
                                        ))
                                },
                                onNameChanged = { name ->
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnNewNameExpCatChanged(name))
                                },
                                onAddButtonClicked = {
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnAddNewExpCatBtnClick)
                                }
                            )
                        } else {
                            NameInputPlus(
                                name = expensesCategoriesState.newCategoryName,
                                onNameChanged = {name ->
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnNewNameExpCatChanged(name))

                                },
                                onAddButtonClicked = {
                                    viewModel.homeScreenEvents(
                                        HomeScreenEvents.OnAddNewExpCatBtnClick)
                                }
                            )
                        }
                    }
                }
                item{
                    Text(text= stringResource(R.string.title_exp_cat))
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
                }
                if(displayExpCategories.isEmpty()){
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.no_categories),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                        //image
                    }
                } else {
                    itemsIndexed(displayExpCategories){ index, item ->
                        InfoCardInput(
                            textFirstR = item.header,
                            textSecondR = { Text(text = item.subHeader)},
                            onNumberChanged = { amountString ->

                            },
                            inputNumber = item.amount.toString(),
                            onAddButtonClicked = {

                            },
                            content = { }
                        )
                    }
                }
            }
            if(homeScreenState.page == HomePages.BookPage){
                item {
                    DateRange(
                        startDate = newBookedState.startDate,
                        endDate = newBookedState.endDate,
                        onCalendarBtnClick = {
                            viewModel.homeScreenEvents(HomeScreenEvents.OnCalendarBtnClick)
                        }
                    )
                }
                item {
                    NameCommentFields(
                        name = newBookedState.name,
                        comment = newBookedState.comment,
                        onNameChange = { name ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnNameBookedChanged(name))
                        },
                        onCommentChange = { comment ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnCommentBookedChanged(comment))
                        }
                    )
                }
                item{
                    PaymentWidget(
                        nights = newBookedState.nights,
                        oneNightMoney = newBookedState.oneNightMoney,
                        allMoney = newBookedState.allMoney,
                        onAllMoneyChange = { moneyString ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnAllMoneyChange(
                                    moneyString.toIntOrNull() ?:0
                                )
                            )
                        },
                        onNightMChange = { moneyString ->
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnOneNightMoneyChange(
                                    moneyString.toIntOrNull() ?:0
                                )
                            )
                        }
                    )
                }
                item{
                    SectionButton(
                        modifier = Modifier.fillMaxWidth(),
                        text= stringResource(R.string.add_btn),
                        isSelected = true,
                        onBtnClick = {
                            viewModel.homeScreenEvents(
                                HomeScreenEvents.OnAddNewBookedBtnClick)
                        }
                    )
                }

            }

        }
    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RentCalendarTheme {
        val repository = HomeRepositoryImpl()
        val useCases = HomeUseCases(
            getFinResults = GetFinResults(repository),
            addNewFlat = AddNewFlat(repository),
            getAllFlats = GetAllFlats(repository),
            updatePaidStatus = UpdatePaidStatus(repository),
            getRentList = GetRentList(repository),
            getSchedulePageState = GetSchedulePageState(),
            addNewExpCat = AddNewExpCat(repository),
            getExpCategories = GetExpCategories(repository),
            addNewBooked = AddNewBooked(repository)
        )
        val viewModel = HomeViewModel(useCases)
        HomeScreen(viewModel)
    }
}