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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melonlemon.rentcalendar.R
import com.melonlemon.rentcalendar.core.data.HomeRepositoryImpl
import com.melonlemon.rentcalendar.core.presentation.components.FilterButton
import com.melonlemon.rentcalendar.core.presentation.components.InputContainer
import com.melonlemon.rentcalendar.core.presentation.components.NameInputPlus
import com.melonlemon.rentcalendar.core.presentation.components.SectionButton
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.AddNewFlat
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.GetAllFlats
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.GetFinResults
import com.melonlemon.rentcalendar.feature_home.domain.use_cases.HomeUseCases
import com.melonlemon.rentcalendar.feature_home.presentation.components.FinanceResultWidget
import com.melonlemon.rentcalendar.feature_home.presentation.util.CheckStatusNewFlat
import com.melonlemon.rentcalendar.feature_home.presentation.util.HomePages
import com.melonlemon.rentcalendar.feature_home.presentation.util.HomeScreenEvents
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
    val selectedFlatId by viewModel.selectedFlatId.collectAsStateWithLifecycle()
    val homeScreenState by viewModel.homeScreenState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessages = mapOf(
        CheckStatusNewFlat.BlankFailStatus to stringResource(R.string.err_msg_empty_flat),
        CheckStatusNewFlat.DuplicateFailStatus to stringResource(R.string.err_msg_duplicate_name),
        CheckStatusNewFlat.SuccessStatus to stringResource(R.string.msg_success_status),
        CheckStatusNewFlat.UnKnownFailStatus to  stringResource(R.string.err_msg_unknown_error)
    )
    val errorStatus = stringResource(R.string.err_msg_unknown_error)

    if(flatsState.checkStatusNewFlat!= CheckStatusNewFlat.UnCheckedStatus){

        LaunchedEffect(flatsState.checkStatusNewFlat){

            snackbarHostState.showSnackbar(
                message = errorMessages[flatsState.checkStatusNewFlat]?: errorStatus,
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
                            isSelected = selectedFlatId == -1,
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
                            isSelected = selectedFlatId == item.id,
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
                    text = if(homeScreenState.yearMonth == currentYearMonth)
                        stringResource(R.string.this_month) + ": ${homeScreenState.yearMonth.month.name} ${homeScreenState.yearMonth.year}"
                else "${homeScreenState.yearMonth.month.name}${homeScreenState.yearMonth.year}",
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

            }
            if(homeScreenState.page == HomePages.ExpensesPage){

            }
            if(homeScreenState.page == HomePages.BookPage){

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
            getAllFlats = GetAllFlats(repository)
        )
        val viewModel = HomeViewModel(useCases)
        HomeScreen(viewModel)
    }
}